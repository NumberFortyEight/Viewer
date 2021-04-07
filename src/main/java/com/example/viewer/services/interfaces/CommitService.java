package com.example.viewer.services.interfaces;

import com.example.viewer.dataClasses.Commit;

import java.util.List;

public interface CommitService {
    List<Commit> getAllCommits(String fullPath);
    List<Commit> getCommitsByPath(String fullPath);
}
