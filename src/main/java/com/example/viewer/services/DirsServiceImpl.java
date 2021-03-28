package com.example.viewer.services;

import com.example.viewer.models.FileModel;
import com.example.viewer.models.FileModelFactory;
import com.example.viewer.services.interfaces.DirsService;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DirsServiceImpl implements DirsService {

    public Optional<List<FileModel>> getFileModelList(String repositoriesPath, String workPath) {
        File dirs = new File(repositoriesPath + workPath);
        if (dirs.exists() && dirs.isDirectory()) {
            List<File> files = Arrays.asList(Objects.requireNonNull(dirs.listFiles()));
            return Optional.of(files.stream()
                    .filter(file -> !file.getName().contains(".wiki"))
                    .map(file -> FileModelFactory.createFileModel(file, workPath)).collect(Collectors.toList()));
        }
        return Optional.empty();
    }
}
