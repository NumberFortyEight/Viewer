package com.example.viewer.services.jgit;

import com.example.viewer.enums.ContentType;
import com.example.viewer.exceptions.JGit.JGitFileLoadException;
import com.example.viewer.dataClasses.Content;
import com.example.viewer.dataClasses.FileModel;
import com.example.viewer.dataClasses.FileFactory;
import com.example.viewer.services.interfaces.JGitProvider;
import com.example.viewer.util.PathHelper;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.lib.ObjectReader;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.treewalk.TreeWalk;
import org.eclipse.jgit.treewalk.filter.PathFilter;
import org.springframework.context.ApplicationContext;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
@RequiredArgsConstructor
public class JGitObjectProducer {

    private String pathToRepository;
    private TreeWalk treeWalk;
    private RevCommit targetCommit;
    private String workPath;
    private Repository repository;
    @NonNull
    private final ApplicationContext appContext;

    public void setFields(RevCommit revCommit, String fullPath) throws IOException {
        Git git = appContext.getBean(JGitProvider.class).getConnection(fullPath);
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
                switch (relevant) {
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

    public Content getObject() throws Exception {
        if (isThisExist()) {
            if (isFile()) {
                return new Content(getContentType(), loadFile());
            } else {
                return new Content(ContentType.JSON, getDirs());
            }
        } else {
            return new Content(ContentType.UNSUPPORTED_FORMAT, null);
        }
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
                toLoad.add(FileFactory.createFileModel(
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
                    toLoad.add(FileFactory.createFileModel(
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
