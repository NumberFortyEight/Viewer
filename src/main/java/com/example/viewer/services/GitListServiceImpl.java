package com.example.viewer.services;

import com.example.viewer.dataClasses.GitUser;
import com.example.viewer.exceptions.UseFileException;
import com.example.viewer.services.interfaces.GitListService;
import org.springframework.beans.factory.annotation.Value;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.stream.Collectors;

public class GitListServiceImpl implements GitListService {

    @Value("${repositories.list.file}")
    public String REPOSITORIES_LIST_FILEPATH;

    @Override
    public void write(GitUser gitUser) {
        try {
            Path path = Path.of(REPOSITORIES_LIST_FILEPATH);
            boolean exist = Files.lines(path).anyMatch(line -> line.equals(gitUser.toString()));
            if (!exist) {
                Files.write(path, gitUser.toString().getBytes(), StandardOpenOption.APPEND);
            }
        } catch (IOException e) {
            throw new UseFileException("fail to write gitUser to file", e);
        }
    }

    @Override
    public String readAll() {
        try {
            Path path = Path.of(REPOSITORIES_LIST_FILEPATH);
            return Files.lines(path).collect(Collectors.joining());
        } catch (IOException e) {
            throw new UseFileException("fail to write gitUser to file", e);
        }

    }
}
