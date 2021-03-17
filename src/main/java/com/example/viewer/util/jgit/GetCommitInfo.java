package com.example.viewer.util.jgit;

import com.example.viewer.util.PathHelper;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.treewalk.TreeWalk;
import org.eclipse.jgit.treewalk.filter.PathFilter;

import java.io.IOException;
import java.util.*;
import java.util.stream.StreamSupport;

public class GetCommitInfo {

    private final Git git;

    public GetCommitInfo(Git git) {
        this.git = git;
    }

    public List<RevCommit> getAllCommits(){
        List<RevCommit> revCommitList = new ArrayList<>();
        try {
            Iterator<RevCommit> commitIterator = git.log().call().iterator();
            while (commitIterator.hasNext()) {
                revCommitList.add(commitIterator.next());
            }
            return revCommitList;
        } catch (GitAPIException gitAPIException){
            return revCommitList;
        }
    }

    public RevCommit getCommitByDate(int unixTime) throws GitAPIException {
        return StreamSupport.stream(
                Spliterators.spliteratorUnknownSize(git.log().call().iterator(), Spliterator.ORDERED),
                false).filter(revCommit -> revCommit.getCommitTime() == unixTime).findFirst().orElse(findFirst());
    }

    private RevCommit findFirst() throws GitAPIException {
        return git.log().call().iterator().next();
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
