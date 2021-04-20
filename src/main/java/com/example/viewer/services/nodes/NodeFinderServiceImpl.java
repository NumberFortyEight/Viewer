package com.example.viewer.services.nodes;

import com.example.viewer.dataClasses.Node;
import com.example.viewer.services.interfaces.NodeTreeFinderService;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Service
public class NodeFinderServiceImpl implements NodeTreeFinderService {

    @Override
    public Optional<Node> getOptionalNodeTreeByUsername(String username, Map<String, Node> userAndNodeTree) {
        return userAndNodeTree.containsKey(username) ? Optional.ofNullable(userAndNodeTree.get(username)) : Optional.empty();
    }
}
