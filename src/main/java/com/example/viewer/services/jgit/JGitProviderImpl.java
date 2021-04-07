package com.example.viewer.services.jgit;

import com.example.viewer.services.interfaces.JGitProvider;
import com.example.viewer.util.PathHelper;
import org.eclipse.jgit.api.Git;
import org.springframework.beans.factory.annotation.Value;

import java.io.File;
import java.io.IOException;

public class JGitProviderImpl implements JGitProvider {

    @Value("${path.to.repositories}")
    private String REPOSITORIES_PATH;

    public Git getConnection(String fullPath) throws IOException {
        String pathToRepository = PathHelper.getAbsolutePath(PathHelper.limit(fullPath, 2));
        return Git.open(new File(REPOSITORIES_PATH + "/" + pathToRepository));
    }

}
