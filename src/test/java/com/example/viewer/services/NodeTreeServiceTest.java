package com.example.viewer.services;

import com.example.viewer.models.Node;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;


class NodeTreeServiceTest {
    @Test
    void setNodeBranch() {
        NodeTreeService nodeTreeService = new NodeTreeService();
        Node node = new Node();
        node.setName("repository");
/*
        nodeTreeService.setNodeDependency(node, "/repository/label/label");
        nodeTreeService.setNodeDependency(node, "/repository/label/secondLabel/layerTwo");
        nodeTreeService.setNodeDependency(node, "repository/vartex/lible/addon");

        Optional<Node> nodeByPath = nodeTreeService.findNodeByPath(node, "/repository");
        System.out.print(nodeByPath);
        Map<String, Node> userAndNodeTree = new HashMap<>();
        userAndNodeTree.put("aa", node);
        userAndNodeTree.get("a");*/
    }
}