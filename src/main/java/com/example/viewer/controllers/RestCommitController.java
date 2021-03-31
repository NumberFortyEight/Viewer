package com.example.viewer.controllers;

import com.example.viewer.models.CommitModel;
import com.example.viewer.services.interfaces.CommitQueryService;
import com.example.viewer.services.jgit.JGitScope;
import com.example.viewer.util.PathHelper;
import io.reflectoring.diffparser.api.model.Diff;
import io.reflectoring.diffparser.api.model.Line;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
public class RestCommitController {

    public final CommitQueryService commitQueryService;

    public RestCommitController(CommitQueryService commitQueryService) {
        this.commitQueryService = commitQueryService;
    }

    @GetMapping("/commit/{student}/{repository}/allCommits")
    public List<CommitModel> getAllCommits(HttpServletRequest request){
        String fullPath = PathHelper.skipAndLimit(request.getRequestURI(),1, 2);
        return new JGitScope(fullPath).getCommitInfo().getCommitModelList();
    }

    @GetMapping("/commit/{student}/{repository}/diff")
    public Object getDiffCommit(HttpServletRequest request){
        String fullPath = PathHelper.skipAndLimit(request.getRequestURI(),1, 2);
        Optional<String> optionalQuery = Optional.ofNullable(request.getQueryString());
        return optionalQuery.map(query -> commitQueryService.getDiff(fullPath, query)).orElse(null);
    }
}
