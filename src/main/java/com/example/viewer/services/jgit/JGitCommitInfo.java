package com.example.viewer.services.jgit;

import com.example.viewer.models.CommitModel;
import com.example.viewer.models.RevCommitPacker;
import com.example.viewer.services.interfaces.JGitProvider;
import com.example.viewer.util.PathHelper;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.diff.RenameDetector;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.treewalk.TreeWalk;
import org.eclipse.jgit.treewalk.filter.PathFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import java.io.IOException;
import java.util.*;
import java.util.stream.StreamSupport;
@Slf4j
@RequiredArgsConstructor
public class JGitCommitInfo {

    private Git git;
    @NonNull
    private final ApplicationContext appContext;

    public void setGitByPath(String fullPath) throws IOException {
        JGitProvider jGitProvider = appContext.getBean(JGitProvider.class);
        this.git = jGitProvider.getConnection(fullPath);
    }

    public List<CommitModel> getAllCommitsModelList() throws GitAPIException {
        return RevCommitPacker.getCommitModelListOfListRevCommit(getAllCommits());
    }

    public RevCommit getCommitByDate(int unixTime) throws GitAPIException {
        return StreamSupport.stream(
                Spliterators.spliteratorUnknownSize(git.log()
                        .call()
                        .iterator(), Spliterator.ORDERED), false)
                .filter(revCommit -> revCommit.getCommitTime() == unixTime)
                .findFirst()
                .orElse(findFirstRevCommit());
    }

    public RevCommit findFirstRevCommit() throws GitAPIException {
        return git.log().call().iterator().next();
    }

    public List<CommitModel> getCommitsByFullPath(String fullPath) throws Exception {
        String workPath = PathHelper.skip(fullPath, 2);
        if (workPath.equals("") || workPath.equals("/")) {
            return getAllCommitsModelList();
        } else {
            return RevCommitPacker.getCommitModelListOfListRevCommit(getCommitsByPath(workPath));
        }
    }

    private ArrayList<RevCommit> getCommitsByPath(String path) throws Exception {
        ArrayList<RevCommit> commits = new ArrayList<>();
        RevCommit start = null;
        do {
            Iterable<RevCommit> log = git.log().addPath(path).call();
            for (RevCommit commit : log) {
                if (commits.contains(commit)) {
                    start = null;
                } else {
                    start = commit;
                    commits.add(commit);
                }
            }
            if (start == null) return commits;
        }
        while ((path = getRenamedPath(start, path)) != null);

        return commits;
    }
    private String getRenamedPath( RevCommit start, String path) throws Exception {
        Iterable<RevCommit> allCommitsLater = git.log().add(start).call();
        for (RevCommit commit : allCommitsLater) {
            Repository repository = git.getRepository();
            TreeWalk tw = new TreeWalk(repository);
            tw.addTree(commit.getTree());
            tw.addTree(start.getTree());
            tw.setRecursive(true);
            RenameDetector rd = new RenameDetector(repository);
            rd.addAll(DiffEntry.scan(tw));
            List<DiffEntry> files = rd.compute();
            for (DiffEntry diffEntry : files) {
                if ((diffEntry.getChangeType() == DiffEntry.ChangeType.RENAME || diffEntry.getChangeType() == DiffEntry.ChangeType.COPY) && diffEntry.getNewPath().contains(path)) {
                    return diffEntry.getOldPath();
                }
            }
        }
        return null;
    }

    public List<String> getPathsInTree(String fullPath, RevCommit targetCommit) throws IOException {
        String workPath = PathHelper.skip(fullPath, 2);
        List<String> toLoad = new ArrayList<>();
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
    }

    public List<RevCommit> getAllCommits() throws GitAPIException {
        List<RevCommit> revCommitList = new ArrayList<>();
        for (RevCommit revCommit : git.log().call()) {
            revCommitList.add(revCommit);
        }
        return revCommitList;
    }
}
