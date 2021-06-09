package com.example.viewer.services;

import com.example.viewer.exceptions.JGit.JGitCommitInfoException;
import com.example.viewer.dataClasses.Commit;
import com.example.viewer.services.interfaces.CommitService;
import com.example.viewer.services.jgit.JGitCommitInfo;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class CommitServiceImpl implements CommitService {
    public final JGitFactoryService jGitFactoryService;

    @Override
    public List<Commit> getAllCommits(String fullPath) {
        try {
            JGitCommitInfo commitInfo = jGitFactoryService.getCommitInfo(fullPath);
            return commitInfo.getAllCommitsModelList();
        } catch (GitAPIException e) {
            log.error("exception with getting all commits of path = '{}', exception message = '{}'",fullPath,e.getMessage());
            throw new JGitCommitInfoException("Exception on find all commits", e);
        }
    }

    @Override
    public List<Commit> getCommitsByPath(String fullPath) {
        try {
            JGitCommitInfo commitInfo = jGitFactoryService.getCommitInfo(fullPath);
            return commitInfo.getCommitsByFullPath(fullPath);
        } catch (Exception e) {
            log.error("exception with getting commit by path = '{}', exception message = '{}'",fullPath,e.getMessage());
            throw new JGitCommitInfoException("Exception on find commits by path", e);
        }
    }
}
