package com.example.viewer.controllers;

import com.example.viewer.enums.FoldersPaths;
import com.example.viewer.models.FileModel;
import com.example.viewer.services.interfaces.DirsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin
public class RestDirsController {

    public String REPOSITORIES_PATH = FoldersPaths.ALL_REPOSITORIES_PATH.getPath();

    private final DirsService dirsService;
    public RestDirsController(DirsService dirsService) {
        this.dirsService = dirsService;
    }

    @GetMapping(value = { "api", "api/{student}"})
    @ResponseBody
    public List<FileModel> getDirectory(@PathVariable Optional<String> student) {
        String url = student.map(existStudent -> "/" + existStudent).orElse("/");
        return dirsService.getFileModelList(REPOSITORIES_PATH, url).orElse(List.of());
    }
}
