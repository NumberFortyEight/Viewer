package com.example.viewer.services;

import com.example.viewer.exception.JGitCommitInfoException;
import com.example.viewer.models.CommitModel;
import com.example.viewer.services.interfaces.CommitService;
import com.example.viewer.services.jgit.JGitCommitInfo;
import lombok.AllArgsConstructor;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
@AllArgsConstructor
public class CommitServiceImpl implements CommitService {
    private final ApplicationContext appContext;

    @Override
    public List<CommitModel> getAllCommits(String fullPath) {
        try {
            JGitCommitInfo jGitCommitInfo = appContext.getBean(JGitCommitInfo.class);
            jGitCommitInfo.setGitByPath(fullPath);
            return jGitCommitInfo.getAllCommitsModelList();
        } catch (IOException | GitAPIException e) {
            throw new JGitCommitInfoException("Exception on find all commits", e);
        }
    }

    @Override
    public List<CommitModel> getCommitsByPath(String fullPath) {
        try {
            JGitCommitInfo jGitCommitInfo = appContext.getBean(JGitCommitInfo.class);
            jGitCommitInfo.setGitByPath(fullPath);
            return jGitCommitInfo.getCommitsByFullPath(fullPath);

        } catch (Exception e) {
            throw new JGitCommitInfoException("Exception on find commits by path", e);
        }
    }
}
