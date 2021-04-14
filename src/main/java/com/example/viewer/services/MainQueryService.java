package com.example.viewer.services;

import com.example.viewer.dataClasses.Node;
import com.example.viewer.services.nodes.CreationOrUpdateNodeTreeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class MainQueryService {
    public final CreationOrUpdateNodeTreeService creationOrUpdateNodeTreeService;

    public void queryLogic(String user, String fullPath, String query, Map<String, Node> userAndNodeTree) {
        String[] split = query.split("=");
        if (split.length >= 2) {
            String queryType = split[0];
            String value = split[1];
            switch (queryType){
                case "commit":
                    creationOrUpdateNodeTreeService.createNodeHierarchy(user, fullPath, Integer.parseInt(value), userAndNodeTree);
                    break;
                case "drop":
                    userAndNodeTree.remove(user);
                    break;
            }
        }
    }

}