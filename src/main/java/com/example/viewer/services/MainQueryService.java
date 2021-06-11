package com.example.viewer.services;

import com.example.viewer.dataClasses.Node;
import com.example.viewer.services.interfaces.PathCommitService;
import lombok.AllArgsConstructor;
import org.eclipse.jgit.revwalk.RevCommit;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@AllArgsConstructor
public class MainQueryService {

    private final PathCommitService pathCommitService;

    public void queryLogic(String user, String fullPath, String query, Map<String, Node> userAndNode) {
        String[] split = query.split("=");
        if (split.length >= 2) {
            String queryType = split[0];
            String value = split[1];
            switch (queryType){
                case "commit":
                    pathCommitService.createPathCommitMap(user, fullPath, Integer.parseInt(value), userAndNode);
                    break;
                case "drop":

                    break;
            }
        }
    }

}
