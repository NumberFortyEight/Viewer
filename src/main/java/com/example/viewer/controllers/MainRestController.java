package com.example.viewer.controllers;

import com.example.viewer.models.Node;
import com.example.viewer.services.NodeCreateService;
import com.example.viewer.services.QueryService;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@ResponseBody
public class MainRestController {

    private final Map<String, Node> userAndNodeTree = new HashMap<>();
    public final NodeCreateService nodeCreateService;
    public final QueryService queryService;

    public MainRestController(NodeCreateService nodeCreateService, QueryService queryService) {
        this.nodeCreateService = nodeCreateService;
        this.queryService = queryService;
    }

    @GetMapping("{student}/{repository}/**")
    @ResponseStatus(HttpStatus.OK)
    public Object doGetObject(HttpServletRequest request) {
        String user = "One";
        String fullPath = request.getRequestURI();
        Optional<String> OptionalQuery = Optional.ofNullable(request.getQueryString());

        OptionalQuery.ifPresent(query -> queryService.queryLogic(user, fullPath, query, userAndNodeTree));

        return userAndNodeTree;

    }

}
