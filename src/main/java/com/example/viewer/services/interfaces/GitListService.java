package com.example.viewer.services.interfaces;

import com.example.viewer.dataClasses.GitUser;

public interface GitListService {
    void write(GitUser gitUser);
    String readAll();
}
