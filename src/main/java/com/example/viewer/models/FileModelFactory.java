package com.example.viewer.models;

import com.example.viewer.enums.State;
import com.example.viewer.models.FileModel;
import com.example.viewer.util.PathHelper;

public class FileModelFactory {
    public FileModel createFileModel(Boolean isFolder, String fileName, String pathToRepository, String PathString){
        State state = isFolder ? State.FOLDER : State.FILE;
        String fullPath = PathHelper.getAbsolutePath(pathToRepository) + PathHelper.getAbsolutePath(PathString);
        return new FileModel(state, fileName, fullPath);
    }
}
