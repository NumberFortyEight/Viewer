package com.example.viewer.services.jgit;

import com.example.viewer.enums.FoldersPaths;
import com.example.viewer.util.PathHelper;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.revwalk.RevCommit;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.io.File;
import java.io.IOException;

public class JGitScope {

    public String REPOSITORIES_PATH = FoldersPaths.ALL_REPOSITORIES_PATH.getPath();
    private final Git git;

    public JGitScope(String fullPath) {
        String pathToRepository = PathHelper.getAbsolutePath(PathHelper.limit(fullPath, 2));
        try {
            this.git = Git.open(new File(REPOSITORIES_PATH + "/" + pathToRepository));
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Git not found:" + pathToRepository);
        }
    }

    public JGitCommitInfo getCommitInfo() {
        return new JGitCommitInfo(git);
    }

    public JGitObjectProducer getObjectProducer(RevCommit revCommit, String fullPath) {
        return new JGitObjectProducer(git, revCommit, fullPath);
    }
}