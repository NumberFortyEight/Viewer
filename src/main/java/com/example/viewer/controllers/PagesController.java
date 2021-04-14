package com.example.viewer.controllers;

import com.example.viewer.util.PathHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@RestController
@CrossOrigin
@RequiredArgsConstructor
public class PagesController {

    @Value("${path.to.teacher.page}")
    private String TEACHER_PAGE_PATH;

    @GetMapping("/teacher/**")
    public byte[] getPageResources(HttpServletRequest request) throws IOException {
        String workPath = PathHelper.skip (request.getRequestURI(), 1);
        return Files.readAllBytes(Paths.get(TEACHER_PAGE_PATH + "/" + workPath));
    }
}
