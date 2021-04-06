package com.example.viewer.controllers;

import com.example.viewer.exception.DirsLookupException;
import com.example.viewer.exception.ExceptionMessage;
import com.example.viewer.exception.JGitCommitInfoException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

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
    public static ExceptionMessage emptyCommitList(JGitCommitInfoException e, HttpServletRequest request) {
        return new ExceptionMessage(LocalDateTime.now(), e.getMessage(), request.getRequestURI());
    }

}