package com.example.viewer.services.interfaces;

import com.example.viewer.dataClasses.Node;

import java.util.Map;

public interface NodeTreeService {
    void createNodeHierarchy(String user, String fullPath, int commitTime, Map<String, Node> userAndNodeTree);
}
