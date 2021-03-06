package com.example.viewer.services;

import com.example.viewer.dataClasses.Content;
import com.example.viewer.dataClasses.Node;
import com.example.viewer.services.interfaces.JGitFacadeService;
import com.example.viewer.services.interfaces.NodeExplorerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jgit.revwalk.RevCommit;
import org.springframework.stereotype.Service;

import java.util.Map;


@Slf4j
@Service
@RequiredArgsConstructor
public class JGitFacadeServiceImpl implements JGitFacadeService {

    private final NodeExplorerService nodeExplorerService;
    private final JGitFactoryService jGitFactoryService;

    public Content getContent(String user, String fullPath, Map<String, Node> userAndNodeTree) {
        RevCommit detectedCommit = nodeExplorerService.findCommitInNodeByPath(user, fullPath, userAndNodeTree);
        return jGitFactoryService.getContent(fullPath, detectedCommit);
    }

}

