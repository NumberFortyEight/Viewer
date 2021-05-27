package com.example.viewer.dataClasses;

import com.example.viewer.dataClasses.enums.State;
import com.example.viewer.util.PathHelper;

import java.io.File;

public class FileFactory {

    public static FileModel createFileModel(Boolean isFolder, String fileName, String pathToRepository,Integer commitTime, String PathString) {
        State state = isFolder ? State.FOLDER : State.FILE;
        String fullPath = PathHelper.getAbsolutePath(pathToRepository) + PathHelper.getAbsolutePath(PathString);
        return new FileModel(state, fileName, commitTime, fullPath);
    }

    public static FileModel createFileModel(File file, String workPath) {
        return new FileModel.FileModelBuilder()
                .name(file.getName())
                .href((!workPath.equals("/") ? workPath + "/" + file.getName() : "/" + file.getName()))
                .state(file.getName().contains(".git") ? State.REPOSITORY : !file.isFile() ? State.FOLDER : State.FILE)
                .commitTime(0)
                .build();
    }
}
