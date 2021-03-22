package com.example.viewer.enums;

public enum PathToAllRepositories {
    ALL_REPOSITORIES_PATH("C:\\Users\\Java\\.gitbucket\\repositories");

    private final String path;

    PathToAllRepositories(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }
}
