package com.example.viewer.services.jgit;

import com.example.viewer.exception.JGitFileLoadException;
import com.example.viewer.models.FileModel;
import com.example.viewer.models.FileModelFactory;
import com.example.viewer.util.PathHelper;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.lib.ObjectReader;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.treewalk.TreeWalk;
import org.eclipse.jgit.treewalk.filter.PathFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class JGitObjectProducer {

    private final String pathToRepository;
    private final TreeWalk treeWalk;
    private final RevCommit targetCommit;
    private final String workPath;
    private final Repository repository;

    public JGitObjectProducer(Git git, RevCommit revCommit, String fullPath) {
        this.repository = git.getRepository();
        this.targetCommit = revCommit;
        this.pathToRepository = PathHelper.getAbsolutePath(PathHelper.limit(fullPath, 2));;
        this.workPath =  PathHelper.skip(fullPath,2);
        this.treeWalk = new TreeWalk(repository);
    }

    public boolean isThisExist() throws IOException {
        if (workPath.equals("") || workPath.equals("/")) {
            return true;
        }
        treeWalk.addTree(targetCommit.getTree());
        treeWalk.setRecursive(false);
        while (treeWalk.next()) {
            if (treeWalk.getPathString().equals(workPath)) {
                return true;
            }
            if (treeWalk.isSubtree()) {
                treeWalk.enterSubtree();
            }
        }
        treeWalk.reset();
        return false;

    }

    public boolean isFile() throws Exception {
        if (workPath.equals("") || workPath.equals("/")) {
            treeWalk.reset();
            return false;
        }
        treeWalk.addTree(targetCommit.getTree());
        treeWalk.setRecursive(false);
        while (treeWalk.next()) {
            if (treeWalk.isSubtree()) {
                if (treeWalk.getPathString().equals(workPath)) {
                    treeWalk.reset();
                    return false;
                }
                treeWalk.enterSubtree();
            } else {
                if (treeWalk.getPathString().equals(workPath)) {
                    treeWalk.reset();
                    return true;
                }
            }
        }
        throw new JGitFileLoadException("unknown state");
    }

    public boolean isImage() throws IOException {
        treeWalk.addTree(targetCommit.getTree());
        treeWalk.setRecursive(true);
        treeWalk.setFilter(PathFilter.create(workPath));
        while (treeWalk.next()) {
            String nameString = treeWalk.getNameString();
            String[] relevant = nameString.split("\\.");
            treeWalk.reset();
            if (relevant.length >= 2) {
                return relevant[1].contains("jpg");
            }
            return false;
        }
        return false;
    }

    public byte[] loadFile() throws IOException {
        treeWalk.addTree(targetCommit.getTree());
        treeWalk.setRecursive(true);
        treeWalk.setFilter(PathFilter.create(workPath));
        try (ObjectReader objectReader = repository.newObjectReader()) {
            while (treeWalk.next()) {
                return objectReader.open(treeWalk.getObjectId(0)).getBytes();
            }
        }
        return null;
    }

    public List<FileModel> getDirs() throws IOException {
        treeWalk.addTree(targetCommit.getTree());
        treeWalk.setRecursive(false);

        String[] split = workPath.split("/");
        List<FileModel> toLoad = new ArrayList<>();

        FileModelFactory fileModelFactory = new FileModelFactory();

        if (workPath.equals("")) {
            while (treeWalk.next()) {
                toLoad.add(fileModelFactory.createFileModel(
                        treeWalk.isSubtree(),
                        treeWalk.getNameString(),
                        pathToRepository,
                        treeWalk.getPathString()));
            }
        } else {
            boolean skip = true;
            int i = 0;
            while (skip && treeWalk.next()) {
                if (treeWalk.isSubtree() && treeWalk.getNameString().equals(split[i])) {
                    treeWalk.enterSubtree();
                    i++;
                    if (treeWalk.getPathString().equals(workPath)) {
                        skip = false;
                    }
                }
            }
            while (treeWalk.next()) {
                if (treeWalk.getPathString().split("/").length > split.length) {
                    toLoad.add(fileModelFactory.createFileModel(
                            treeWalk.isSubtree(),
                            treeWalk.getNameString(),
                            pathToRepository,
                            treeWalk.getPathString()));
                }
            }
        }
        return toLoad;
    }


}
