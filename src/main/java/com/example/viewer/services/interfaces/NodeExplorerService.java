package com.example.viewer.services.interfaces;

import com.example.viewer.dataClasses.Node;
import org.eclipse.jgit.revwalk.RevCommit;

import java.util.Map;

public interface NodeExplorerService {
    RevCommit findCommitInNodeTreeByPath(String user, String fullPath, Map<String, Node> userAndNodeTree);
}
