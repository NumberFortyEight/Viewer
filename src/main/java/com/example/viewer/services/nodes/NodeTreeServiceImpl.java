package com.example.viewer.services.nodes;

import com.example.viewer.dataClasses.Node;
import com.example.viewer.services.JGitFactoryService;
import com.example.viewer.services.interfaces.NodeTreeFinderService;
import com.example.viewer.services.interfaces.NodeTreeService;
import com.example.viewer.services.interfaces.NodeUpdateService;
import com.example.viewer.util.PathHelper;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.eclipse.jgit.revwalk.RevCommit;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Service
@AllArgsConstructor
public class NodeTreeServiceImpl implements NodeTreeService {

    public final NodeTreeFinderService nodeTreeFinderService;
    public final NodeTreeUpdateServiceImpl nodeTreeUpdateService;
    public final JGitFactoryService jGitFactoryService;

    // TODO: 16.04.2021
    //fix Sneaky
    @SneakyThrows
    @Override
    public void createNodeHierarchy(String username, String fullPath, int commitTime, Map<String, Node> userAndNodeTree) {
        String repositoryName = PathHelper.skipAndLimit(fullPath, 1, 1);
        Optional<Node> optionalNodeTree = nodeTreeFinderService.getOptionalNodeTreeByUsername(username, userAndNodeTree);

        if (optionalNodeTree.isPresent()) {
            Node existNode = optionalNodeTree.get();
            boolean isCurrentRepository = existNode
                    .getName()
                    .equals(repositoryName);
            if (isCurrentRepository) {
                Node node = nodeTreeUpdateService.updateNodeTree(existNode, repositoryName, fullPath, commitTime, userAndNodeTree);
            }

        }
    }
}
