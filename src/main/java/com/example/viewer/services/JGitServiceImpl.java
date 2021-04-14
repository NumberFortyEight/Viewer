package com.example.viewer.services;

import com.example.viewer.exceptions.JGit.JGitException;
import com.example.viewer.dataClasses.Content;
import com.example.viewer.dataClasses.Node;
import com.example.viewer.services.interfaces.JGitService;
import com.example.viewer.services.interfaces.NodeExplorerService;
import com.example.viewer.services.jgit.JGitObjectProducer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jgit.revwalk.RevCommit;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class JGitServiceImpl implements JGitService {

    private final NodeExplorerService nodeExplorerService;
    private final ApplicationContext appContext;

    public Content getContent(String user, String fullPath, Map<String, Node> userAndNodeTree) {
        try {
            RevCommit fundedCommit = nodeExplorerService.findCommitInNodeTreeByPath(user, fullPath, userAndNodeTree);
            JGitObjectProducer jGitObjectProducer = appContext.getBean(JGitObjectProducer.class);
            jGitObjectProducer.setFields(fundedCommit, fullPath);
            return jGitObjectProducer.getObject();
        } catch (Exception e) {
            log.warn("Content load fail on path: " + fullPath, e);
            throw new JGitException( "Content load fail on path: " + fullPath, e);
        }
    }

}
