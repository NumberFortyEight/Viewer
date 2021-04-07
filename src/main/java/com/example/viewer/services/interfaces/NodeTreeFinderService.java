package com.example.viewer.services.interfaces;

import com.example.viewer.dataClasses.Node;

import java.util.Map;
import java.util.Optional;

public interface NodeTreeFinderService {
    Optional<Node> getOptionalNodeTreeByUsername(String user, Map<String, Node> userAndNodeTree);
    Node getExistOrNewNode(String user, String repositoryName, Map<String, Node> userAndNodeTree);
}
