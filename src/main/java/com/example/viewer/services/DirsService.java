package com.example.viewer.services;

import com.example.viewer.enums.State;
import com.example.viewer.models.FileModel;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
@Service
public class DirsService {
    public List<FileModel> getFileModelList(String repositoriesPath, String workPath) {
        File dirs = new File(repositoriesPath + workPath);
        if (dirs.exists() && dirs.isDirectory()) {
            List<File> files = Arrays.asList(Objects.requireNonNull(dirs.listFiles()));
            return files.stream()
                    //fixme ".viki"
                    .filter(file -> !file.getName().contains(".wiki"))
                    .map(file -> new FileModel.FileModelBuilder()
                            .withName(file.getName())
                            .withHref( (!workPath.equals("/") ? workPath + "/" + file.getName() : "/" + file.getName()))
                            .withState(file.getName().contains(".git") ? State.REPOSITORY : !file.isFile() ? State.FOLDER : State.FILE)
                            .build()).collect(Collectors.toList());
        }
        return null;
    }
}
