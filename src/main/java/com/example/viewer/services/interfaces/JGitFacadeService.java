package com.example.viewer.services.interfaces;

import com.example.viewer.dataClasses.Content;
import org.eclipse.jgit.revwalk.RevCommit;

import java.util.HashMap;
import java.util.Map;

public interface JGitFacadeService {
    Content getContent(String user, String fullPath, Map<String, HashMap<String, RevCommit>> userAndNodeTree);
}
