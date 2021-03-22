package com.example.viewer.services;

import com.example.viewer.models.Node;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.Map;

@Service
public class QueryService {
    public final NodeCreateService nodeCreateService;

    public QueryService(NodeCreateService nodeCreateService) {
        this.nodeCreateService = nodeCreateService;
    }

    public void queryLogic(String user, String fullPath, String query, Map<String, Node> userAndNodeTree) {
        try {
            String[] split = query.split("=");
            if (split.length >= 2) {
                String queryType = split[0];
                String value = split[1];
                switch (queryType){
                    case "commit":
                        nodeCreateService.createNodeHierarchy(user, fullPath, Integer.parseInt(value), userAndNodeTree);
                        break;
                    case "drop":
                        userAndNodeTree.remove(user);
                        break;
                }
            }
        } catch (IOException | GitAPIException e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "bad request: " + fullPath);
        }
    }

}
