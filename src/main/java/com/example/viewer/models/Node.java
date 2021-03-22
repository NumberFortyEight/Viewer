package com.example.viewer.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.eclipse.jgit.revwalk.RevCommit;

import java.util.ArrayList;
import java.util.List;


@JsonIgnoreProperties(value = { "parentNode" , "revCommit"})
public class Node {
    private String name = "";
    private List<Node> childNodeList = new ArrayList<>();
    private Node parentNode;
    private RevCommit revCommit;
    private int commitTime;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Node> getChildNodeList() {
        return childNodeList;
    }

    public void setChildNodeList(List<Node> childNodeList) {
        this.childNodeList = childNodeList;
    }

    public Node getParentNode() {
        return parentNode;
    }

    public void setParentNode(Node parentNode) {
        this.parentNode = parentNode;
    }

    public RevCommit getRevCommit() {
        return revCommit;
    }

    public int getCommitTime() {
        return commitTime;
    }

    public void setCommitTime(int commitTime) {
        this.commitTime = commitTime;
    }

    public void setRevCommit(RevCommit revCommit) {
        this.revCommit = revCommit;
        setCommitTime(revCommit.getCommitTime());
    }
}
