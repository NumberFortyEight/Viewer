package com.example.viewer.services.interfaces;

import com.example.viewer.dataClasses.Node;

import java.util.Map;
import java.util.Optional;

public interface NodeTreeFinderService {
    Optional<Node> getOptionalNodeTreeByUsername(String username, Map<String, Node> userAndNodeTree);
    Node getExistOrNewNode(String username, String repositoryName, Map<String, Node> userAndNodeTree);
}
