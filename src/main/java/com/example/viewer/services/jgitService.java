package com.example.viewer.services;

import com.example.viewer.models.Node;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class jgitService {


    public final QueryService queryService;
    public final Map<String, Node> userAndNodeTree = new HashMap<>();

    @Autowired
    public jgitService(QueryService queryService) {
        this.queryService = queryService;
    }

    //return CommitLoader
    public void logicGates(String user, String fullPath, Optional<String> optionalQuery){
        //optionalQuery.ifPresent(query -> queryService.queryLogic(user, fullPath, query, userAndNodeTree));



    }

}
