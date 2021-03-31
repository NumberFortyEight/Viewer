package com.example.viewer.services.jgit;

import com.example.viewer.exception.JGitCommitInfoException;
import com.example.viewer.models.CommitModel;
import com.example.viewer.util.PathHelper;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.diff.DiffFormatter;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.treewalk.TreeWalk;
import org.eclipse.jgit.treewalk.filter.PathFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.stream.StreamSupport;

public class JGitCommitInfo {

    private static final Logger LOGGER = LoggerFactory.getLogger(JGitCommitInfo.class);

    private Git git;

    public JGitCommitInfo(Git git) {
        this.git = git;
    }

    public List<RevCommit> getAllCommits() {
        List<RevCommit> revCommitList = new ArrayList<>();
        try {
            for (RevCommit revCommit : git.log().call()) {
                revCommitList.add(revCommit);
            }
            return revCommitList;
        } catch (GitAPIException gitAPIException) {
            return revCommitList;
        }
    }

    public String getDiffOrNull(int unixTime) {
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

            try (DiffFormatter diffFormatter = new DiffFormatter(byteArrayOutputStream)) {
                diffFormatter.setRepository(git.getRepository());
                Optional<RevCommit> optionalParentCommit = getAllCommits().stream()
                        .filter(revCommit -> revCommit.getCommitTime() < unixTime)
                        .findFirst();
                for (DiffEntry entry : diffFormatter.scan(getCommitByDate(unixTime), optionalParentCommit.orElse(findFirstRevCommit()))) {
                    diffFormatter.format(diffFormatter.toFileHeader(entry));
                }
                System.out.println(byteArrayOutputStream);
                return byteArrayOutputStream.toString();
            }
        } catch (Exception e){
            LOGGER.warn("Diff Exception", e);
            return null;
        }
    }

    public List<CommitModel> getCommitModelList() {
        ArrayList<CommitModel> commitModelArrayList = new ArrayList<>();
        try {
            for (RevCommit nextCommit : git.log().call()) {
                commitModelArrayList.add(new CommitModel(nextCommit.getFullMessage(),
                        nextCommit.getAuthorIdent().getName(), String.valueOf(nextCommit.getCommitTime())));
            }
        } catch (GitAPIException gitAPIException) {
            LOGGER.warn("Commits model load exception ", gitAPIException);
        }
        return commitModelArrayList;
    }

    public RevCommit getCommitByDate(int unixTime) {
        try {
            return StreamSupport.stream(
                    Spliterators.spliteratorUnknownSize(git.log().call().iterator(), Spliterator.ORDERED),
                    false).filter(revCommit -> revCommit.getCommitTime() == unixTime).findFirst().orElse(findFirstRevCommit());
        } catch (GitAPIException e) {
            throw new JGitCommitInfoException("Commit by date exception", e);
        }
    }

    public RevCommit findFirstRevCommit() {
        try {
            return git.log().call().iterator().next();
        } catch (GitAPIException e) {
            throw new JGitCommitInfoException("first commit exception", e);
        }
    }

    public List<String> getPathsInTree(String fullPath, RevCommit targetCommit) {
        String workPath = PathHelper.skip(fullPath, 2);
        List<String> toLoad = new ArrayList<>();
        try {
            TreeWalk treeWalk = new TreeWalk(git.getRepository());
            treeWalk.addTree(targetCommit.getTree());
            treeWalk.setRecursive(true);
            if (!workPath.equals("")) {
                treeWalk.setFilter(PathFilter.create(workPath));
            }
            while (treeWalk.next()) {
                toLoad.add(treeWalk.getPathString());
            }
            treeWalk.reset();
            return toLoad;
        } catch (IOException e) {
            LOGGER.warn("Empty path", e);
            return toLoad;
        }
    }
}
