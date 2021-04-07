package com.example.viewer.services.interfaces;

import com.example.viewer.models.CommitModel;

import java.util.List;

public interface CommitService {
    List<CommitModel> getAllCommits(String fullPath);
    List<CommitModel> getCommitsByPath(String fullPath);
}
