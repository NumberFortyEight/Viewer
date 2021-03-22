package com.example.viewer.controllers;

import com.example.viewer.enums.PathToAllRepositories;
import com.example.viewer.models.FileModel;
import com.example.viewer.services.DirsService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin
public class RestDirsController {

    public String REPOSITORIES_PATH = PathToAllRepositories.ALL_REPOSITORIES_PATH.getPath();

    private final DirsService dirsService;

    public RestDirsController(DirsService dirsService) {
        this.dirsService = dirsService;
    }

    @GetMapping(value = { "/", "{student}"})
    @ResponseBody
    public List<FileModel> getDirectory(@PathVariable Optional<String> student) {
        String url = student.map(existStudent -> "/" + existStudent).orElse("/");
        return dirsService.getFileModelList(REPOSITORIES_PATH, url);
    }
}
