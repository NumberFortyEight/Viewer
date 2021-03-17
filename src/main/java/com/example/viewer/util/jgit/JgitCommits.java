package com.example.viewer.util.jgit;

import com.example.viewer.enums.Paths;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class JgitCommits {

    public String REPOSITORIES_PATH = Paths.ALL_REPOSITORIES_PATH.getPath();

    public GetCommitInfo getInfo(String pathToRepository) {
        try {
            return new GetCommitInfo(Git.open(new File(REPOSITORIES_PATH + "/" + pathToRepository)));
        } catch (IOException ignored){
            return null;
        }
    }

}
