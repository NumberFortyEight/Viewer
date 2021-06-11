package com.example.viewer.services;

import com.example.viewer.dataClasses.Node;
import com.example.viewer.exceptions.JGit.JGitCommitInfoException;
import com.example.viewer.services.interfaces.PathCommitService;
import com.example.viewer.util.PathHelper;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.revwalk.RevCommit;
import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@AllArgsConstructor
public class PathCommitServiceImpl implements PathCommitService {

    private final JGitFactoryService jGitFactoryService;
    @SneakyThrows
    @Override
    public void createPathCommitMap(String user, String fullPath, int unixTime, Map<String, Node> userAndNode) {
        Optional<Node> optionalNode = Optional.ofNullable(userAndNode.get(user));

        String author = PathHelper.limit(fullPath, 1);
        String repositoryName = PathHelper.skipAndLimit(fullPath, 1, 1);
        RevCommit commitByDate = jGitFactoryService.getCommitInfo(fullPath).getCommitByDate(unixTime);

        if (optionalNode.isEmpty()) {
            Node node = createNode(author, repositoryName, commitByDate);
            userAndNode.put(user, node);
        } else {
            Node existNode = optionalNode.get();
            if (existNode.getAuthor().equals(author) && existNode.getRepositoryName().equals(repositoryName)) {
                if (isPathToRepository(fullPath)) {
                    existNode.setMainCommit(commitByDate);
                } else {
                    existNode.getExclusion().put(fullPath, commitByDate);
                }
            } else {
                Node node = createNode(author, repositoryName, commitByDate);

            }

        }
    }

    @Override
    public RevCommit getCommitByPath(String user, String fullPath, Map<String, HashMap<String, RevCommit>> userAndURlsWithCommits) {
        try {
            HashMap<String, RevCommit> pathRevCommitHashMap =
                    Optional.ofNullable(userAndURlsWithCommits.get(user)).orElse(new HashMap<>());
            if (pathRevCommitHashMap.containsKey(fullPath)) {
                return pathRevCommitHashMap.get(fullPath);
            }
            return jGitFactoryService.getCommitInfo(fullPath).getFirstRevCommit();
        } catch (GitAPIException e) {
            throw new JGitCommitInfoException("Exception on load commits", e);
        }
    }

    private Node createNode(String author, String repositoryName, RevCommit commitByDate) {
        Node node = new Node();
        node.setAuthor(author);
        node.setRepositoryName(repositoryName);
        node.setExclusion(new HashMap<>());
        node.setMainCommit(commitByDate);
        return node;
    }

    private boolean isPathToRepository(String fullPath) {
        return fullPath.endsWith(".git") || fullPath.endsWith(".git/");
    }
}
