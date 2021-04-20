package com.example.viewer.services.nodes;

import com.example.viewer.dataClasses.Node;
import com.example.viewer.exceptions.JGit.JGitCommitInfoException;
import com.example.viewer.services.JGitFactoryService;
import com.example.viewer.services.interfaces.NodeUsingService;
import com.example.viewer.services.jgit.JGitCommitInfo;
import com.example.viewer.util.PathHelper;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.revwalk.RevCommit;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;

@Service
@AllArgsConstructor
public class NodeUsingServiceImpl implements NodeUsingService {

    private final WorkWithNodeService workWithNodeService;
    private final JGitFactoryService jGitFactoryService;

    @Override
    public Node fillNodeTree(Node node, String repositoryName, String fullPath, int commitTime, Map<String, Node> userAndNodeTree) {
        String workPathWithRepository = PathHelper.skip(fullPath, 1);
        JGitCommitInfo commitInfo = jGitFactoryService.getCommitInfo(fullPath);
        try {
            RevCommit commitByDate = commitInfo.getCommitByDate(commitTime);

            commitInfo.getPathsInTree(fullPath, commitByDate)
                    .forEach(path -> workWithNodeService
                            .setNodeDependency(node, repositoryName + PathHelper.getAbsolutePath(path)));

            workWithNodeService
                    .setCommitToNodeTree(workWithNodeService.findNodeByPath(node, workPathWithRepository)
                            .orElse(node), commitByDate);
            return node;
        } catch (GitAPIException | IOException e) {
            throw new JGitCommitInfoException("fail in fill node tree", e);
        }
    }
}
