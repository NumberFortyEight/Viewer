package com.example.viewer.services;

import com.example.viewer.models.Node;
import com.example.viewer.util.PathHelper;
import org.eclipse.jgit.revwalk.RevCommit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class JgitService {

    public void loadFileOrDirs(String user, String fullPath, Map<String, Node> userAndNodeTree){
        String workPathWithRepository = PathHelper.skip(fullPath, 1);
        Node node = userAndNodeTree.get(user);
    }

    public RevCommit targetCommit(String workPath, Node node){
        try {
            String[] pathNuggets = PathHelper.getRelativePath(workPath).split("/");
            if (node.getName().equals(pathNuggets[0])) {
                Optional<Node> optionalCommonChild = node.getChildNodeList().stream().filter(childNode -> childNode.getName().equals(pathNuggets[1])).findFirst();
                return optionalCommonChild.isPresent() ? targetCommit(PathHelper.skip(workPath,1), optionalCommonChild.get()) : node.getRevCommit();
            } else {
                return node.getRevCommit();
            }
        } catch (NullPointerException | ArrayIndexOutOfBoundsException e){
            return node.getRevCommit();
        }
    }
}
