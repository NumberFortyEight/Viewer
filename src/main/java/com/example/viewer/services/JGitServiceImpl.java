package com.example.viewer.services;

import com.example.viewer.models.ContentModel;
import com.example.viewer.models.Node;
import com.example.viewer.services.interfaces.JGitService;
import com.example.viewer.services.interfaces.NodeExplorerService;
import com.example.viewer.services.jgit.JGitObjectProducer;
import com.example.viewer.services.jgit.JGitScope;
import org.eclipse.jgit.revwalk.RevCommit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

@Service
public class JGitServiceImpl implements JGitService {
    private static final Logger LOGGER = LoggerFactory.getLogger(JGitServiceImpl.class);

    final NodeExplorerService nodeExplorerService;

    public JGitServiceImpl(NodeExplorerService nodeExplorerService) {
        this.nodeExplorerService = nodeExplorerService;
    }

    public ContentModel getContent(String user, String fullPath, Map<String, Node> userAndNodeTree) {
        try {
            RevCommit fundedCommit = nodeExplorerService.findCommitInNodeTreeByPath(user, fullPath, userAndNodeTree);
            return new JGitScope(fullPath).getObjectProducer(fundedCommit, fullPath).getObject();
        } catch (Exception e) {
            LOGGER.warn("Content load fail on path: " + fullPath, e);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Content load fail on path: " + fullPath);
        }
    }

}
