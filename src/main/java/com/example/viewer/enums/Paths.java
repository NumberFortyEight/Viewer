package com.example.viewer.enums;

public enum Paths {
    ALL_REPOSITORIES_PATH("C:\\repositories"), TEACHER_PAGE_PATH("C:\\teacherPage");
    private final String path;

    Paths(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }
}
