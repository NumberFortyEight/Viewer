package com.example.viewer.dataClasses;

import org.eclipse.jgit.revwalk.RevCommit;

import java.util.List;
import java.util.stream.Collectors;

public class RevCommitPacker {
    public static List<Commit> getCommitModelListOfListRevCommit(List<RevCommit> revCommitList){
        return revCommitList.stream().map(revCommit ->
                new Commit(revCommit.getFullMessage(),
                    revCommit.getAuthorIdent().getName(), String.valueOf(revCommit.getCommitTime())))
                .collect(Collectors.toList()) ;
    }
}
