package com.example.viewer.services.interfaces;

import org.eclipse.jgit.revwalk.RevCommit;

import java.util.HashMap;
import java.util.Map;

public interface PathCommitService {
    void createPathCommitMap(String user, String fullPath, String query, Map<String, HashMap<String, RevCommit>> userAndURlsWithCommits);
    RevCommit getCommitByPath(String user, String fullPath,Map<String, HashMap<String, RevCommit>> userAndURlsWithCommits);
}
