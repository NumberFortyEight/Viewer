package com.example.viewer.services;

import com.example.viewer.dataClasses.GitUser;
import com.example.viewer.exceptions.JGit.JGitAddRepositoryException;
import com.example.viewer.services.interfaces.GitUserSaveService;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class GitUserSaveServiceImpl implements GitUserSaveService {

    @Value("${path.to.repositories}")
    private String REPOSITORIES_PATH;

    public void validateGitUser(GitUser gitUser){
        try {
            Git.cloneRepository()
                    .setURI("https://github.com/" + gitUser.getNickname() +"/" + gitUser.getRepository())
                    .setBare(true)
                    .setDirectory(new File(REPOSITORIES_PATH + "/"
                            + gitUser.getName() + "/"
                            + gitUser.getRepository()))
                    .call();
        } catch (GitAPIException e) {
            throw new JGitAddRepositoryException("repository add exception", e);
        }
    }
}
