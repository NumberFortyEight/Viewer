package com.example.viewer.services;

import com.example.viewer.exceptions.JGit.JGitCommitInfoException;
import com.example.viewer.services.interfaces.JGitFacadeService;
import com.example.viewer.services.interfaces.PathCommitService;
import lombok.AllArgsConstructor;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.revwalk.RevCommit;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@AllArgsConstructor
public class PathCommitServiceImpl implements PathCommitService {

    private final JGitFactoryService jGitFactoryService;
    @Override
    public void createPathCommitMap(String user, String fullPath, String unixTime, Map<String, HashMap<String, RevCommit>> userAndURlsWithCommits) {
        try {
            HashMap<String, RevCommit> pathRevCommitHashMap =
                    Optional.ofNullable(userAndURlsWithCommits.get(user)).orElse(new HashMap<>());
            RevCommit commitByDate = jGitFactoryService.getCommitInfo(fullPath).getCommitByDate(Integer.parseInt(unixTime));
            pathRevCommitHashMap.put(fullPath, commitByDate);
            userAndURlsWithCommits.put(user, pathRevCommitHashMap);
        } catch (GitAPIException e) {
            e.printStackTrace();
        }
    }

    @Override
    public RevCommit getCommitByPath(String user, String fullPath, Map<String, HashMap<String, RevCommit>> userAndURlsWithCommits) {
        try {
            HashMap<String, RevCommit> pathRevCommitHashMap =
                    Optional.ofNullable(userAndURlsWithCommits.get(user)).orElse(new HashMap<>());
            if (pathRevCommitHashMap.containsKey(fullPath)) {
                return pathRevCommitHashMap.get(fullPath);
            }
            return jGitFactoryService.getCommitInfo(fullPath).getFirstRevCommit();
        } catch (GitAPIException e) {
            throw new JGitCommitInfoException("Exception on load commits", e);
        }
    }
}
