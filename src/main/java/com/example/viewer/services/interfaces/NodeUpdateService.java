package com.example.viewer.services.interfaces;

import com.example.viewer.dataClasses.Node;
import org.eclipse.jgit.revwalk.RevCommit;

import java.util.Map;

public interface NodeUpdateService {
    Node updateNodeTree(Node node, String repositoryName, String fullPath,int commitTime, Map<String, Node> userAndNodeTree);
}
