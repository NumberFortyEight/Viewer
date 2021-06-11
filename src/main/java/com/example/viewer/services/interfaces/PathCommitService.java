package com.example.viewer.services.interfaces;

import com.example.viewer.dataClasses.Node;
import org.eclipse.jgit.revwalk.RevCommit;

import java.util.HashMap;
import java.util.Map;

public interface PathCommitService {
    void createPathCommitMap(String user, String fullPath, int unixTime, Map<String, Node> userAndURlsWithCommits);
    RevCommit getCommitByPath(String user, String fullPath,Map<String, HashMap<String, RevCommit>> userAndURlsWithCommits);
}
