package com.example.viewer.services.nodes;

import com.example.viewer.dataClasses.Node;
import com.example.viewer.exceptions.JGit.JGitException;
import com.example.viewer.services.JGitFactoryService;
import com.example.viewer.services.interfaces.NodeTreeFinderService;
import com.example.viewer.util.PathHelper;
import com.example.viewer.services.jgit.JGitCommitInfo;
import lombok.RequiredArgsConstructor;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.revwalk.RevCommit;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

@Service
@RequiredArgsConstructor
public class CreationOrUpdateNodeTreeService {

    private final NodeTreeFinderService nodeTreeFinderService;
    private final JGitFactoryService jGitFactoryService;

    public void createNodeHierarchy(String username, String fullPath, int unixTime, Map<String, Node> userAndNodeTree) {
        String repositoryName = PathHelper.skipAndLimit(fullPath, 1, 1);
        String workPathWithRepository = PathHelper.skip(fullPath, 1);

        Node workNode = nodeTreeFinderService.getExistOrNewNode(username, repositoryName, userAndNodeTree);
        try {
            JGitCommitInfo commitInfo = jGitFactoryService.getCommitInfo(fullPath);
            RevCommit commitByDate = commitInfo.getCommitByDate(unixTime);

            commitInfo.getPathsInTree(fullPath, commitByDate)
                    .forEach(path -> setNodeDependency(workNode, repositoryName + PathHelper.getAbsolutePath(path)));

            setCommitToNodeTree(findNodeByPath(workNode, workPathWithRepository).orElse(workNode), commitByDate);

            userAndNodeTree.put(username, workNode);

        } catch (IOException | GitAPIException e) {
            throw new JGitException("Jgit error", e);
        }
    }

    public void setCommitToNodeTree(Node node, RevCommit revCommit) {
        node.setRevCommit(revCommit);
        node.getChildNodeList().forEach(child -> {
            child.setRevCommit(revCommit);
            setCommitToNodeTree(child, revCommit);
        });
    }

    public Optional<Node> findNodeByPath(Node node, String path) {
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

    public void setNodeDependency(Node node, String path) {
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

    public Optional<Node> findCommonChild(Node node, String childName) {
        return node.getChildNodeList()
                .stream()
                .filter(child -> child.getName().equals(childName))
                .findFirst();
    }

    public void setNodeBranch(Node fatherNode, String workPath) {
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
