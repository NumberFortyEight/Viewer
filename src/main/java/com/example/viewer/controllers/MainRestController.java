package com.example.viewer.controllers;

import com.example.viewer.models.Node;
import com.example.viewer.services.NodeCreateService;
import com.example.viewer.services.QueryService;
import com.example.viewer.util.PathHelper;
import com.example.viewer.util.jgit.GetCommitInfo;
import com.example.viewer.util.jgit.JgitCommits;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@ResponseBody
@CrossOrigin
public class MainRestController {

    private final Map<String, Node> userAndNodeTree = new HashMap<>();
    public final NodeCreateService nodeCreateService;
    public final QueryService queryService;

    public MainRestController(NodeCreateService nodeCreateService, QueryService queryService) {
        this.nodeCreateService = nodeCreateService;
        this.queryService = queryService;
    }

    @GetMapping("/{student}/{repository}/allCommits")
    public Object doGetAllCommits(HttpServletRequest request){
        GetCommitInfo info = new JgitCommits().getInfo(PathHelper.limit(request.getRequestURI(),2));
        return info.getAllCommits();
    }

    //@GetMapping("{student}/{repository}/**")
    //ResponseStatus(HttpStatus.OK)
    public Object doGetObject(HttpServletRequest request) {
        String user = "One";
        String fullPath = request.getRequestURI();
        Optional<String> OptionalQuery = Optional.ofNullable(request.getQueryString());

        OptionalQuery.ifPresent(query -> queryService.queryLogic(user, fullPath, query, userAndNodeTree));

        return userAndNodeTree;
    }

}
