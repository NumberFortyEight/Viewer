package com.example.viewer.enums;

public enum FoldersPaths {
    ALL_REPOSITORIES_PATH("C:\\Users\\Java\\.gitbucket\\repositories"), TEACHER_PAGE_PATH("C:\\teacherPage");
    private final String path;

    FoldersPaths(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }
}
