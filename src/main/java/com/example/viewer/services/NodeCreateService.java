package com.example.viewer.services;

import com.example.viewer.models.Node;
import com.example.viewer.util.PathHelper;
import com.example.viewer.util.jgit.GetCommitInfo;
import com.example.viewer.util.jgit.JgitCommits;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.revwalk.RevCommit;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

@Service
public class NodeCreateService {

    public void createNodeHierarchy(String user, String fullPath, int unixTime, Map<String, Node> userAndNodeTree) throws IOException, GitAPIException {
        Optional<Node> optionalExistingNode = existNode(user, userAndNodeTree);
        String repositoryName = PathHelper.skipAndLimit(fullPath, 1, 1);

        Node node = new Node();
        node.setName(repositoryName);

        GetCommitInfo info = new JgitCommits().getInfo(PathHelper.getAbsolutePath(PathHelper.limit(fullPath, 2)));
        RevCommit commitByDate = info.getCommitByDate(unixTime);

        info.getPathsInTree(fullPath, commitByDate)
                .forEach(path -> nodeSetter(optionalExistingNode.orElse(node), repositoryName + "/" + path));

        commitSetter(findNodeByPath(optionalExistingNode.orElse(node), PathHelper.skip(fullPath, 1)), commitByDate);
        userAndNodeTree.put(user, optionalExistingNode.orElse(node));
    }

    private Node findNodeByPath(Node node, String path) {
        String[] pathNuggets = path.split("/");
        if (node.getName().equals(pathNuggets[0])) {
            if (pathNuggets.length > 1) {
                Optional<Node> optionalChildNode = node.getChildNodeList().stream().filter(child -> child.getName().equals(pathNuggets[1])).findFirst();
                return optionalChildNode.map(value -> findNodeByPath(value, PathHelper.skip(path, 1))).orElse(node);
            }
        }
        return node;
    }

    private void commitSetter(Node node, RevCommit revCommit) {
        node.setRevCommit(revCommit);
        node.getChildNodeList().forEach(child -> {
            child.setRevCommit(revCommit);
            commitSetter(child, revCommit);
        });
    }

    private void nodeSetter(Node node, String path) {
        String[] pathNuggets = path.split("/");
        if (node.getName().equals(pathNuggets[0])) {
            if (node.getChildNodeList().isEmpty()) {
                branchSetter(node, PathHelper.skip(path, 1));
            } else {
                if (node.getChildNodeList().stream().anyMatch(child -> child.getName().equals(pathNuggets[1]))) {
                    Optional<Node> optionalChild = node.getChildNodeList().stream().filter(child -> child.getName().equals(pathNuggets[1])).findFirst();
                    optionalChild.ifPresent(child -> nodeSetter(child, PathHelper.skip(path, 1)));
                } else branchSetter(node, PathHelper.skip(path, 1));
            }
        } else {
            branchSetter(node, path);
        }
    }

    private void branchSetter(Node fatherNode, String workPath) {
        String[] pathNuggets = workPath.split("/");
        if (!pathNuggets[0].equals("")) {
            Node node = new Node();
            node.setParentNode(fatherNode);
            fatherNode.getChildNodeList().add(node);
            node.setName(pathNuggets[0]);
            if (pathNuggets.length > 1) {
                Node childNode = new Node();
                childNode.setName(pathNuggets[1]);
                childNode.setParentNode(node);
                node.getChildNodeList().add(childNode);
                branchSetter(childNode, PathHelper.skip(workPath, 2));
            }
        }
    }


    private Optional<Node> existNode(String user, Map<String, Node> userAndNodeTree) {
        return userAndNodeTree.containsKey(user) ? Optional.ofNullable(userAndNodeTree.get(user)) : Optional.empty();
    }
}
