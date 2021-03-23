package com.example.viewer.services;

import com.example.viewer.models.Node;
import com.example.viewer.util.PathHelper;
import com.example.viewer.services.jgit.GetCommitInfo;
import com.example.viewer.services.jgit.JgitCommits;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.revwalk.RevCommit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

@Service
public class NodeTreeService {

    private static final Logger LOGGER = LoggerFactory.getLogger(NodeTreeService.class);

    public void createNodeHierarchy(String user, String fullPath, int unixTime, Map<String, Node> userAndNodeTree) {
        String repositoryName = PathHelper.skipAndLimit(fullPath, 1, 1);
        String workPath = PathHelper.getAbsolutePath(PathHelper.limit(fullPath, 2));
        String workPathWithRepository = PathHelper.skip(fullPath, 1);

        Node workNode = getExistOrNewNode(user, repositoryName, userAndNodeTree);

        GetCommitInfo info = new JgitCommits().getInfo(workPath);
        RevCommit commitByDate = info.getCommitByDate(unixTime);

        info.getPathsInTree(fullPath, commitByDate)
                .forEach(path -> setNodeDependency(workNode, repositoryName + PathHelper.getAbsolutePath(path)));

        setCommitToNodeTree(findNodeByPath(workNode, workPathWithRepository).orElse(workNode), commitByDate);

       userAndNodeTree.put(user, workNode);
    }

    public Node getExistOrNewNode(String user, String repositoryName, Map<String, Node> userAndNodeTree){
        Node existNode = userAndNodeTree.get(user);
        if (existNode != null) {
            String existNodeName = existNode.getName();
            if (!existNodeName.equals(repositoryName)) {
                userAndNodeTree.remove(user);

                Node node = new Node();
                node.setName(repositoryName);
                return node;
            } else {
                return existNode;
            }
        } else {
            Node node = new Node();
            node.setName(repositoryName);
            return node;
        }
    }

    //public
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

    //public
    public void setNodeDependency(Node node, String path) {
        String[] pathNuggets = PathHelper.getRelativePath(path).split("/");
        if (node.getName().equals(pathNuggets[0])) {
            if (node.getChildNodeList().isEmpty()) {
                setNodeBranch(node, PathHelper.skip(path, 1));
            } else {
                Optional<Node> optionalCommonChild = findCommonChild(node, pathNuggets[1]);

                if (optionalCommonChild.isPresent()) {
                    setNodeDependency(optionalCommonChild.get(), PathHelper.skip(path, 1));
                } else {
                    setNodeBranch(node, PathHelper.skip(path, 1));
                }
            }
        } else {
            setNodeBranch(node, path);
        }
    }

    //public
    public Optional<Node> findCommonChild(Node node, String childName) {
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
