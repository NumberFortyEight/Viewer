package com.example.viewer.services;

import com.example.viewer.dataClasses.Node;
import com.example.viewer.services.interfaces.PathCommitService;
import com.example.viewer.util.PathHelper;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.eclipse.jgit.revwalk.RevCommit;
import org.springframework.stereotype.Service;

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

        if (optionalNode.isPresent()) {
            Node existNode = optionalNode.get();
            if (existNode.getAuthor().equals(author) && existNode.getRepositoryName().equals(repositoryName)) {
                if (isPathToRepository(fullPath)) {
                    existNode.setMainCommit(commitByDate);
                } else {
                    existNode.getExclusion().put(fullPath, commitByDate);
                }
            }
        }
        Node node = createNode(author, repositoryName, commitByDate);
        userAndNode.put(user, node);
    }

    @SneakyThrows
    @Override
    public RevCommit getCommitByPath(String user, String fullPath, Map<String, Node> userAndNode) {
        Optional<Node> optionalNode = Optional.ofNullable(userAndNode.get(user));

        String author = PathHelper.limit(fullPath, 1);
        String repositoryName = PathHelper.skipAndLimit(fullPath, 1, 1);

        if (optionalNode.isPresent()) {
            Node existNode = optionalNode.get();
            if (existNode.getAuthor().equals(author) && existNode.getRepositoryName().equals(repositoryName)) {
                if (isPathToRepository(fullPath)) {
                    return existNode.getMainCommit();
                } else {
                    Optional<String> pathInExclusions = findPathInExclusions(fullPath, existNode.getExclusion());
                    if (pathInExclusions.isEmpty()) {
                        return existNode.getMainCommit();
                    } else {
                        RevCommit revCommit = existNode.getExclusion().get(pathInExclusions.get());
                        existNode.getExclusion().remove(pathInExclusions.get());
                        return revCommit;
                    }
                }
            }
        }
        return jGitFactoryService.getCommitInfo(fullPath).getFirstRevCommit();
    }

    private Optional<String> findPathInExclusions(String fullPath, HashMap<String, RevCommit> exclusion) {
        return exclusion.keySet().stream().filter(fullPath::contains).findFirst();
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
