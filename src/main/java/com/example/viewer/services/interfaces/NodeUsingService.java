package com.example.viewer.services.interfaces;

import com.example.viewer.dataClasses.Node;
import org.eclipse.jgit.revwalk.RevCommit;

import java.util.Map;

public interface NodeUsingService {
    Node fillNodeTree(Node node, String repositoryName, String fullPath, int commitTime, Map<String, Node> userAndNodeTree);
}
