package com.example.viewer.models;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CommitModel {
    private String message;
    private String author;
    private String simpleDateFormat;
}
