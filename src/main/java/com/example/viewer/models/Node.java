package com.example.viewer.models;

import org.eclipse.jgit.revwalk.RevCommit;

import java.util.List;

public class Node {
    private String name;
    private List<Node> childNodeList;
    private Node parentNode;
    private RevCommit revCommit;

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

    public void setRevCommit(RevCommit revCommit) {
        this.revCommit = revCommit;
    }
}
