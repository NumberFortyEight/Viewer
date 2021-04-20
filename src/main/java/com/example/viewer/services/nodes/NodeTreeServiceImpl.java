package com.example.viewer.services.nodes;

import com.example.viewer.dataClasses.Node;
import com.example.viewer.services.JGitFactoryService;
import com.example.viewer.services.interfaces.NodeTreeFinderService;
import com.example.viewer.services.interfaces.NodeTreeService;
import com.example.viewer.util.PathHelper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Service
@AllArgsConstructor
public class NodeTreeServiceImpl implements NodeTreeService {

    public final NodeTreeFinderService nodeTreeFinderService;
    public final NodeUsingServiceImpl nodeTreeUsingService;
    public final JGitFactoryService jGitFactoryService;

    @Override
    public void createNodeHierarchy(String username, String fullPath, int commitTime, Map<String, Node> userAndNodeTree) {
        String repositoryName = PathHelper.skipAndLimit(fullPath, 1, 1);
        Optional<Node> optionalNodeTree = nodeTreeFinderService.getOptionalNodeTreeByUsername(username, userAndNodeTree);

        Node currentNode;
        if (optionalNodeTree.isPresent()) {
            Node existNode = optionalNodeTree.get();
            boolean isCurrentRepository = existNode
                    .getName()
                    .equals(repositoryName);

            if (isCurrentRepository) {
                currentNode = nodeTreeUsingService.fillNodeTree(existNode, repositoryName, fullPath, commitTime, userAndNodeTree);
                userAndNodeTree.put(username, currentNode);
                return;
            } else {
                userAndNodeTree.remove(username);
            }
        }
        Node node = new Node();
        node.setName(repositoryName);
        currentNode = nodeTreeUsingService.fillNodeTree(node, repositoryName, fullPath, commitTime, userAndNodeTree);
        userAndNodeTree.put(username, currentNode);
    }
}
