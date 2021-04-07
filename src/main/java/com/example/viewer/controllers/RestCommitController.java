package com.example.viewer.controllers;

import com.example.viewer.models.CommitModel;
import com.example.viewer.services.interfaces.CommitQueryService;
import com.example.viewer.services.interfaces.CommitService;
import com.example.viewer.services.jgit.JGitCommitInfo;
import com.example.viewer.util.PathHelper;
import lombok.RequiredArgsConstructor;
import org.eclipse.jgit.revwalk.RevCommit;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class RestCommitController {

    public final CommitQueryService commitQueryService;
    private final CommitService commitService;

    @GetMapping("/commit/{student}/{repository}/allcommits")
    public List<CommitModel> getAllCommits(HttpServletRequest request) {
        return commitService.getAllCommits(PathHelper.getFullPath(request.getRequestURI()));
    }
    @GetMapping("commit/{student}/{repository}/**")
    public List<CommitModel> getCommitsByPath(HttpServletRequest request){
        return commitService.getCommitsByPath(PathHelper.getFullPath(request.getRequestURI()));
    }
}
