package com.example.viewer.services.jgit;

import com.example.viewer.enums.ContentType;
import com.example.viewer.exception.JGitFileLoadException;
import com.example.viewer.models.ContentModel;
import com.example.viewer.models.FileModel;
import com.example.viewer.models.FileModelFactory;
import com.example.viewer.services.interfaces.JGitProvider;
import com.example.viewer.util.PathHelper;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.lib.ObjectReader;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.treewalk.TreeWalk;
import org.eclipse.jgit.treewalk.filter.PathFilter;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class JGitObjectProducer {

    private final String pathToRepository;
    private final TreeWalk treeWalk;
    private final RevCommit targetCommit;
    private final String workPath;
    private final Repository repository;

    public JGitObjectProducer(RevCommit revCommit, String fullPath, JGitProvider jGitProvider) {
        Git git = jGitProvider.getConnection(fullPath);
        this.repository = git.getRepository();
        this.targetCommit = revCommit;
        this.pathToRepository = PathHelper.getAbsolutePath(PathHelper.limit(fullPath, 2));
        this.workPath = PathHelper.skip(fullPath, 2);
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

    public ContentType getContentType() throws IOException {
        treeWalk.addTree(targetCommit.getTree());
        treeWalk.setRecursive(true);
        treeWalk.setFilter(PathFilter.create(workPath));
        while (treeWalk.next()) {
            String nameString = treeWalk.getNameString();
            String[] split = nameString.split("\\.");
            if (split.length >= 2) {
                String relevant = split[1];
                treeWalk.reset();
                switch (relevant){
                    case ("gif"):
                    case ("png"):
                    case ("jpg"):
                        return ContentType.IMAGE;
                    case ("mp4"):
                    case ("avi"):
                        return ContentType.VIDEO;
                    default:
                        return ContentType.BYTES;
                }
            }
        }
        treeWalk.reset();
        return ContentType.UNSUPPORTED_FORMAT;
    }

    public ContentModel getObject(){
        try {
            if (isThisExist()) {
                if (isFile()) {
                    return new ContentModel(getContentType(), loadFile());
                } else {
                    return new ContentModel(ContentType.JSON, getDirs());
                }
            }
        } catch (Exception ignored){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Object not found" + workPath);
        }
        return new ContentModel(ContentType.UNSUPPORTED_FORMAT, null);
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

        if (workPath.equals("")) {
            while (treeWalk.next()) {
                toLoad.add(FileModelFactory.createFileModel(
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
                    toLoad.add(FileModelFactory.createFileModel(
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
