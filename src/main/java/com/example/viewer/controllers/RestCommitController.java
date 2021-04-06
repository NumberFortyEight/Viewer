package com.example.viewer.controllers;

import com.example.viewer.models.CommitModel;
import com.example.viewer.services.interfaces.CommitQueryService;
import com.example.viewer.services.jgit.JGitCommitInfo;
import com.example.viewer.util.PathHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class RestCommitController {

    public final CommitQueryService commitQueryService;
    private final ApplicationContext appContext;

    @GetMapping("/commit/{student}/{repository}/allCommits")
    public List<CommitModel> getAllCommits(HttpServletRequest request){
        String fullPath = PathHelper.skipAndLimit(request.getRequestURI(),1, 2);
        return appContext.getBean(JGitCommitInfo.class, fullPath).getCommitModelList();
    }

    @GetMapping("/commit/{student}/{repository}/diff")
    public Object getDiffCommit(HttpServletRequest request){
        String fullPath = PathHelper.skipAndLimit(request.getRequestURI(),1, 2);
        Optional<String> optionalQuery = Optional.ofNullable(request.getQueryString());
        return optionalQuery.map(query -> commitQueryService.getDiff(fullPath, query)).orElse(null);
    }
}
