package com.example.viewer.services.jgit;

import com.example.viewer.exception.JGitException;
import com.example.viewer.exception.JGitOpenException;
import com.example.viewer.services.interfaces.JGitProvider;
import com.example.viewer.util.PathHelper;
import org.eclipse.jgit.api.Git;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.io.File;
import java.io.IOException;

public class JGitProviderImpl implements JGitProvider {

    @Value("${path.to.repositories}")
    private String REPOSITORIES_PATH;

    public Git getConnection(String fullPath) {
        String pathToRepository = PathHelper.getAbsolutePath(PathHelper.limit(fullPath, 2));
        try {
            return Git.open(new File(REPOSITORIES_PATH + "/" + pathToRepository));
        } catch (IOException e) {
            throw new JGitOpenException( "Git not found:" + pathToRepository, e);
        }
    }

}
