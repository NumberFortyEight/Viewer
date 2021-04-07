package com.example.viewer.services.nodes;

import com.example.viewer.dataClasses.Node;
import com.example.viewer.services.interfaces.NodeTreeFinderService;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Service
public class NodeTreeFinderServiceImpl implements NodeTreeFinderService {

    @Override
    public Optional<Node> getOptionalNodeTreeByUsername(String username, Map<String, Node> userAndNodeTree) {
        return userAndNodeTree.containsKey(username) ? Optional.ofNullable(userAndNodeTree.get(username)) : Optional.empty();
    }

    @Override
    public Node getExistOrNewNode(String username, String repositoryName, Map<String, Node> userAndNodeTree) {
        Node existNode = userAndNodeTree.get(username);
        if (existNode != null) {
            String existNodeName = existNode.getName();
            if (!existNodeName.equals(repositoryName)) {
                userAndNodeTree.remove(username);

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
}
