package com.example.viewer.services.jgit;

import com.example.viewer.enums.PathToAllRepositories;
import com.example.viewer.services.NodeCreateService;
import com.example.viewer.util.PathHelper;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.revwalk.RevCommit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.io.File;
import java.io.IOException;

public class JgitCommits {

    private static final Logger LOGGER = LoggerFactory.getLogger(NodeCreateService.class);
    public String REPOSITORIES_PATH = PathToAllRepositories.ALL_REPOSITORIES_PATH.getPath();

    public GetCommitInfo getInfo(String pathToRepository) {
        try {
            return new GetCommitInfo(Git.open(new File(REPOSITORIES_PATH + "/" + pathToRepository)));
        } catch (IOException e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Git not found:" + pathToRepository);
        }
    }

    public JGitObjectProducer getJGitObject(RevCommit revCommit, String fullPath){
        String pathToRepository = PathHelper.getAbsolutePath(PathHelper.limit(fullPath, 2));
        try {
            String workPath = PathHelper.skip(fullPath,2);
            Git git = Git.open(new File(REPOSITORIES_PATH + "/" + pathToRepository));
            return new JGitObjectProducer(git, revCommit, pathToRepository, workPath);
        } catch (IOException e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "ObjectProducer error:" + pathToRepository);
        }
    }
}
