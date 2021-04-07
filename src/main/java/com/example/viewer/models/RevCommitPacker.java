package com.example.viewer.models;

import com.example.viewer.models.CommitModel;
import org.eclipse.jgit.revwalk.RevCommit;

import java.util.List;
import java.util.stream.Collectors;

public class RevCommitPacker {
    public static List<CommitModel> getCommitModelListOfListRevCommit(List<RevCommit> revCommitList){
        return revCommitList.stream().map(revCommit ->
                new CommitModel(revCommit.getFullMessage(),
                    revCommit.getAuthorIdent().getName(), String.valueOf(revCommit.getCommitTime())))
                .collect(Collectors.toList()) ;
    }
}
