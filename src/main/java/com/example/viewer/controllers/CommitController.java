package com.example.viewer.controllers;

import com.example.viewer.dataClasses.Commit;
import com.example.viewer.services.interfaces.CommitService;
import com.example.viewer.util.PathHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class CommitController {

    private final CommitService commitService;

    @GetMapping("/commit/{student}/{repository}/allcommits")
    public List<Commit> getAllCommits(HttpServletRequest request) {
        return commitService.getAllCommits(PathHelper.getFullPath(request.getRequestURI()));
    }
    @GetMapping("commit/{student}/{repository}/**")
    public List<Commit> getCommitsByPath(HttpServletRequest request){
        return commitService.getCommitsByPath(PathHelper.getFullPath(request.getRequestURI()));
    }
}
