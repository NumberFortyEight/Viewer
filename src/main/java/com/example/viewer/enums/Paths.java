package com.example.viewer.enums;

public enum Paths {
    ALL_REPOSITORIES_PATH("C:/Users/Java/.gitbucket/repositories");

    private String path;

    Paths(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }
}
