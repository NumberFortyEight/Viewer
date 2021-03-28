package com.example.viewer.services.interfaces;

import com.example.viewer.models.ContentModel;
import com.example.viewer.models.Node;

import java.util.Map;

public interface JGitService {
    ContentModel getContent(String user, String fullPath, Map<String, Node> userAndNodeTree);
}
