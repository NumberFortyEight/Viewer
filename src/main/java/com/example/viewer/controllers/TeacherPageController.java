package com.example.viewer.controllers;

import com.example.viewer.enums.Paths;
import com.example.viewer.util.PathHelper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@RestController
public class TeacherPageController {

    @GetMapping("/teacher/**")
    public byte[] getPageResources(HttpServletRequest request) throws IOException {
        String workPath = PathHelper.skip(request.getRequestURI(), 1);
        return Files.readAllBytes(Path.of(Paths.TEACHER_PAGE_PATH.getPath() + "/" + workPath));
    }
}
