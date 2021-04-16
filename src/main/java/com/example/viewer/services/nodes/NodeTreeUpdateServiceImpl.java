package com.example.viewer.services.nodes;

import com.example.viewer.dataClasses.Node;
import com.example.viewer.services.JGitFactoryService;
import com.example.viewer.services.interfaces.NodeUpdateService;
import com.example.viewer.services.jgit.JGitCommitInfo;
import com.example.viewer.util.PathHelper;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.eclipse.jgit.revwalk.RevCommit;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@AllArgsConstructor
public class NodeTreeUpdateServiceImpl implements NodeUpdateService {

    private final CreationOrUpdateNodeTreeService creationOrUpdateNodeTreeService;
    private final JGitFactoryService jGitFactoryService;

    // TODO: 16.04.2021
    @SneakyThrows
    @Override
    public Node updateNodeTree(Node node, String repositoryName, String fullPath, int commitTime, Map<String, Node> userAndNodeTree) {
        String workPathWithRepository = PathHelper.skip(fullPath, 1);
        JGitCommitInfo commitInfo = jGitFactoryService.getCommitInfo(fullPath);
        RevCommit commitByDate = commitInfo.getCommitByDate(commitTime);

        commitInfo.getPathsInTree(fullPath, commitByDate)
                .forEach(path -> creationOrUpdateNodeTreeService.setNodeDependency(node, repositoryName + PathHelper.getAbsolutePath(path)));

        creationOrUpdateNodeTreeService
                .setCommitToNodeTree(creationOrUpdateNodeTreeService.findNodeByPath(node, workPathWithRepository)
                        .orElse(node), commitByDate);
        return node;
    }
}
