package com.example.viewer.services;

import com.example.viewer.dataClasses.Content;
import com.example.viewer.exceptions.JGit.JGitCommitInfoException;
import com.example.viewer.exceptions.JGit.JGitException;
import com.example.viewer.services.jgit.JGitCommitInfo;
import com.example.viewer.services.jgit.JGitObjectProducer;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jgit.revwalk.RevCommit;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Slf4j
@Service
@AllArgsConstructor
public class JGitFactoryService {
    private final ApplicationContext appContext;

    public JGitCommitInfo getCommitInfo(String fullPath){
        try {
            JGitCommitInfo jGitCommitInfo = appContext.getBean(JGitCommitInfo.class);
            jGitCommitInfo.setGitByPath(fullPath);
            return jGitCommitInfo;
        } catch (IOException e) {
            log.warn("fail to create commit info by path {}", fullPath);
            throw new JGitCommitInfoException("utility jgit commit class fail", e);
        }
    }

    public Content getContent(String fullPath, RevCommit foundCommit){
        try {
            JGitObjectProducer jGitObjectProducer = appContext.getBean(JGitObjectProducer.class);
            jGitObjectProducer.setFields(foundCommit, fullPath);
            return jGitObjectProducer.getObject();
        } catch (Exception e) {
            log.warn("fail to create object producer by path {}", fullPath);
            throw new JGitException("utility jgit object class fail", e);
        }
    }
}
