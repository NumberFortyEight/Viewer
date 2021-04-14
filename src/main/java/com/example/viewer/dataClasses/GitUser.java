package com.example.viewer.dataClasses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

@Data
@AllArgsConstructor
@ToString
public class GitUser {
    private String name;
    private String nickname;
    private String repository;
}
