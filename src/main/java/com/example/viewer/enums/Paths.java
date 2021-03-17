package com.example.viewer.enums;

public enum Paths {
    ALL_REPOSITORIES_PATH("C:\\repositories");

    private String path;

    Paths(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }
}
