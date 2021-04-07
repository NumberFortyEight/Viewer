package com.example.viewer.services.interfaces;

import com.example.viewer.dataClasses.Content;
import com.example.viewer.dataClasses.Node;

import java.util.Map;

public interface JGitService {
    Content getContent(String user, String fullPath, Map<String, Node> userAndNodeTree);
}
