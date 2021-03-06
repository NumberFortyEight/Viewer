package com.example.viewer.services.interfaces;

import com.example.viewer.dataClasses.FileModel;

import java.util.List;
import java.util.Optional;

public interface DirsService {
    Optional<List<FileModel>> getFileModelList(String pathToFind, String workPath);
}
