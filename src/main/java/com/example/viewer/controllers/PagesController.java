package com.example.viewer.controllers;

import com.example.viewer.dataClasses.GitUser;
import com.example.viewer.services.interfaces.GitListService;
import com.example.viewer.services.interfaces.GitUserSaveService;
import com.example.viewer.util.PathHelper;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

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

    @NonNull
    private final GitUserSaveService gitUserSaveService;
    @NonNull
    private final GitListService gitListService;

    @GetMapping("/teacher/**")
    public byte[] getPageResources(HttpServletRequest request) throws IOException {
        String workPath = PathHelper.skip(request.getRequestURI(), 1);
        return Files.readAllBytes(Paths.get(TEACHER_PAGE_PATH + "/" + workPath));
    }

    @PostMapping("/teacher/addUser")
    @ResponseStatus(HttpStatus.OK)
    public void addUserRepository(@ModelAttribute GitUser gitUser){
        gitUserSaveService.validateGitUser(gitUser);
        gitListService.write(gitUser);
    }
}
