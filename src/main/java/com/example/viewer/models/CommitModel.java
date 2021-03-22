package com.example.viewer.models;

public class CommitModel {
    private String message;
    private String author;
    private String simpleDateFormat;

    public CommitModel(String message, String author, String unixTime) {
        this.message = message;
        this.author = author;
        this.simpleDateFormat = unixTime;
    }

    public CommitModel() {
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getSimpleDateFormat() {
        return simpleDateFormat;
    }

    public void setSimpleDateFormat(String simpleDateFormat) {
        this.simpleDateFormat = simpleDateFormat;
    }
}
