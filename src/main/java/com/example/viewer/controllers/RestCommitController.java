package com.example.viewer.controllers;

import com.example.viewer.models.CommitModel;
import com.example.viewer.services.jgit.JGitScope;
import com.example.viewer.util.PathHelper;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public class RestCommitController {
    @GetMapping("/{student}/{repository}/allCommits")
    public List<CommitModel> doGetAllCommits(HttpServletRequest request){
        String fullPath = PathHelper.limit(request.getRequestURI(),2);
        return new JGitScope(fullPath).getCommitInfo().getCommitModelList();
    }
}
