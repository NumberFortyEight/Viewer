package com.example.viewer.controllers;

import com.example.viewer.exceptions.*;
import com.example.viewer.exceptions.JGit.JGitCommitInfoException;
import com.example.viewer.exceptions.JGit.JGitOpenException;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

@RestControllerAdvice
public class AdviceExceptionController {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public static ExceptionMessage handleWhateverException(DirsLookupException e, HttpServletRequest request) {
        return new ExceptionMessage(LocalDateTime.now(), e.getMessage(), request.getRequestURI());
    }
    
    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ExceptionMessage emptyCommitList(HttpServletRequest request, JGitCommitInfoException e) {
        return new ExceptionMessage(LocalDateTime.now(), e.getMessage(), request.getRequestURI());
    }
    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public static ExceptionMessage gitOpenException(JGitOpenException e, HttpServletRequest request) {
        return new ExceptionMessage(LocalDateTime.now(), e.getMessage(), request.getRequestURI());
    }
    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionMessage jgitException(HttpServletRequest request, GitAPIException e) {
        return new ExceptionMessage(LocalDateTime.now(), e.getMessage(), request.getRequestURI());
    }

}