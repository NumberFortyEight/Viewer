package com.example.viewer.dataClasses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.eclipse.jgit.revwalk.RevCommit;

import java.util.HashMap;

@Getter
@Setter
@AllArgsConstructor
public class Node {
    private String author;
    private String repositoryName;
    private HashMap<String, RevCommit> exclusion;
    private RevCommit mainCommit;
}


