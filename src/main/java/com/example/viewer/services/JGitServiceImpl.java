package com.example.viewer.services;

import com.example.viewer.exceptions.JGitException;
import com.example.viewer.dataClasses.Content;
import com.example.viewer.dataClasses.Node;
import com.example.viewer.services.interfaces.JGitService;
import com.example.viewer.services.interfaces.NodeExplorerService;
import com.example.viewer.services.jgit.JGitObjectProducer;
import lombok.RequiredArgsConstructor;
import org.eclipse.jgit.revwalk.RevCommit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class JGitServiceImpl implements JGitService {
    private static final Logger LOGGER = LoggerFactory.getLogger(JGitServiceImpl.class);

    private final NodeExplorerService nodeExplorerService;
    private final ApplicationContext appContext;

    public Content getContent(String user, String fullPath, Map<String, Node> userAndNodeTree) {
        try {
            RevCommit fundedCommit = nodeExplorerService.findCommitInNodeTreeByPath(user, fullPath, userAndNodeTree);
            JGitObjectProducer jGitObjectProducer = appContext.getBean(JGitObjectProducer.class);
            jGitObjectProducer.setFields(fundedCommit, fullPath);
            return jGitObjectProducer.getObject();
        } catch (Exception e) {
            LOGGER.warn("Content load fail on path: " + fullPath, e);
            throw new JGitException( "Content load fail on path: " + fullPath, e);
        }
    }

}
