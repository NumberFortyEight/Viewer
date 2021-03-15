package com.example.viewer.services;

import com.example.viewer.models.Node;
import com.example.viewer.util.PathHelper;
import com.example.viewer.util.jgit.GetCommitInfo;
import com.example.viewer.util.jgit.JgitCommits;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.revwalk.RevCommit;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;

@Service
public class NodeCreateService {

    public void createNodeHierarchy(String user, String fullPath, int unixTime, Map<String, Node> userAndNodeTree) throws IOException, GitAPIException {
        Optional<Node> optionalExistingNodeHierarchy = existNode(user, userAndNodeTree);

        GetCommitInfo info = new JgitCommits().getInfo(PathHelper.getAbsolutePath(PathHelper.limit(fullPath,2)));

        RevCommit commitByDate = info.getCommitByDate(unixTime);
        info.getPathsInTree(fullPath, commitByDate);
    }

    private Optional<Node> existNode(String user, Map<String, Node> userAndNodeTree){
        return userAndNodeTree.containsKey(user) ? Optional.ofNullable(userAndNodeTree.get(user)) : Optional.empty();
    }
}
