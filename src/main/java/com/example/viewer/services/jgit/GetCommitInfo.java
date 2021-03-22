package com.example.viewer.services.jgit;

import com.example.viewer.models.CommitModel;
import com.example.viewer.services.NodeCreateService;
import com.example.viewer.util.PathHelper;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.treewalk.TreeWalk;
import org.eclipse.jgit.treewalk.filter.PathFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.*;
import java.util.stream.StreamSupport;

public class GetCommitInfo {

    private static final Logger LOGGER = LoggerFactory.getLogger(NodeCreateService.class);

    private final Git git;

    public GetCommitInfo(Git git) {
        this.git = git;
    }

    public List<RevCommit> getAllCommits(){
    List<RevCommit> revCommitList = new ArrayList<>();
        try {
            for (RevCommit revCommit : git.log().call()) {
                revCommitList.add(revCommit);
            }
            return revCommitList;
        } catch (GitAPIException gitAPIException){
            return revCommitList;
        }
    }

    public List<CommitModel> getCommitModelList() {
        ArrayList<CommitModel> commitModelArrayList = new ArrayList<>();
        try {
            for (RevCommit nextCommit : git.log().call()) {
                commitModelArrayList.add(new CommitModel(nextCommit.getFullMessage(),
                        nextCommit.getAuthorIdent().getName(), String.valueOf(nextCommit.getCommitTime())));
            }
        } catch (GitAPIException gitAPIException){
            LOGGER.warn("Commits load error ", gitAPIException);
        }
        return commitModelArrayList;
    }

    public RevCommit getCommitByDate(int unixTime) throws GitAPIException {
        return StreamSupport.stream(
                Spliterators.spliteratorUnknownSize(git.log().call().iterator(), Spliterator.ORDERED),
                false).filter(revCommit -> revCommit.getCommitTime() == unixTime).findFirst().orElse(findFirstRevCommit());
    }

    public RevCommit findFirstRevCommit() {
        try {
            return git.log().call().iterator().next();
        } catch (GitAPIException gitAPIException){
            //exception handler
            return null;
        }
    }

    public List<String> getPathsInTree(String fullPath, RevCommit targetCommit) throws IOException {
        String workPath = PathHelper.skip(fullPath,2);

        TreeWalk treeWalk = new TreeWalk(git.getRepository());
        treeWalk.addTree(targetCommit.getTree());
        treeWalk.setRecursive(true);
        if (!workPath.equals("")) {
            treeWalk.setFilter(PathFilter.create(workPath));
        }
        List<String> toLoad = new ArrayList<>();
        while (treeWalk.next()) {
            toLoad.add(treeWalk.getPathString());
        }
        treeWalk.reset();
        return toLoad;
    }
}