package com.example.viewer.services;

import com.example.viewer.models.Node;
import com.example.viewer.services.interfaces.NodeTreeFinderService;
import com.example.viewer.util.PathHelper;
import com.example.viewer.services.jgit.JGitCommitInfo;
import com.example.viewer.services.jgit.JGitScope;
import org.eclipse.jgit.revwalk.RevCommit;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CreationOrUpdateNodeTreeService {

    public final NodeTreeFinderService nodeTreeFinderService;

    public CreationOrUpdateNodeTreeService(NodeTreeFinderService nodeTreeFinderService) {
        this.nodeTreeFinderService = nodeTreeFinderService;
    }

    public void createNodeHierarchy(String user, String fullPath, int unixTime, Map<String, Node> userAndNodeTree) {
        String repositoryName = PathHelper.skipAndLimit(fullPath, 1, 1);
        String workPathWithRepository = PathHelper.skip(fullPath, 1);

        Node workNode = nodeTreeFinderService.getExistOrNewNode(user, repositoryName, userAndNodeTree);

        JGitCommitInfo info = new JGitScope(fullPath).getCommitInfo();
        RevCommit commitByDate = info.getCommitByDate(unixTime);

        info.getPathsInTree(fullPath, commitByDate)
                .forEach(path -> setNodeDependency(workNode, repositoryName + PathHelper.getAbsolutePath(path)));

        setCommitToNodeTree(findNodeByPath(workNode, workPathWithRepository).orElse(workNode), commitByDate);

        userAndNodeTree.put(user, workNode);
    }

    private void setCommitToNodeTree(Node node, RevCommit revCommit) {
        node.setRevCommit(revCommit);
        node.getChildNodeList().forEach(child -> {
            child.setRevCommit(revCommit);
            setCommitToNodeTree(child, revCommit);
        });
    }

    private Optional<Node> findNodeByPath(Node node, String path) {
        String[] pathNuggets = PathHelper.getRelativePath(path).split("/");
        if (node.getName().equals(pathNuggets[0])) {
            if (pathNuggets.length > 1) {
                Optional<Node> optionalChildNode = findCommonChild(node, pathNuggets[1]);
                String pathWithoutThisNode = PathHelper.skip(path, 1);
                if (!pathWithoutThisNode.equals(""))
                    return optionalChildNode
                            .map(childNode -> findNodeByPath(childNode, pathWithoutThisNode))
                            .orElse(Optional.of(node));
            } else {
                return Optional.of(node);
            }
        }
        return Optional.empty();
    }

    private void setNodeDependency(Node node, String path) {
        String[] pathNuggets = PathHelper.getRelativePath(path).split("/");
        if (node.getName().equals(pathNuggets[0])) {
            if (node.getChildNodeList().isEmpty()) {
                if(pathNuggets.length > 1) setNodeBranch(node, PathHelper.skip(path, 1));
            } else {
                Optional<Node> optionalCommonChild = findCommonChild(node, pathNuggets[1]);

                if (optionalCommonChild.isPresent()) {
                    String skip = PathHelper.skip(path, 1);
                    setNodeDependency(optionalCommonChild.get(), skip);
                } else {
                    setNodeBranch(node, PathHelper.skip(path, 1));
                }
            }
        } else {
            setNodeBranch(node, path);
        }
    }

    private Optional<Node> findCommonChild(Node node, String childName) {
        return node.getChildNodeList()
                .stream()
                .filter(child -> child.getName().equals(childName))
                .findFirst();
    }

    private void setNodeBranch(Node fatherNode, String workPath) {
        String[] pathNuggets = PathHelper.getRelativePath(workPath).split("/");
        Node node = new Node();
        node.setParentNode(fatherNode);
        node.setName(pathNuggets[0]);
        fatherNode.getChildNodeList().add(node);
        if (pathNuggets.length > 1) {
            Node childNode = new Node();
            childNode.setName(pathNuggets[1]);
            childNode.setParentNode(node);
            node.getChildNodeList().add(childNode);
            if (pathNuggets.length > 2) setNodeBranch(childNode, PathHelper.skip(workPath, 2));
        }
    }
}
