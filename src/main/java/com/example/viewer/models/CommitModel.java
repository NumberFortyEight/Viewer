package com.example.viewer.models;

import lombok.Data;

@Data
public class CommitModel {
    private String message;
    private String author;
    private String simpleDateFormat;
}
