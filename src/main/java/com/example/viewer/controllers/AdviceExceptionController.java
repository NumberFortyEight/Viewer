package com.example.viewer.controllers;

import com.example.viewer.exception.DirsLookupException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class AdviceExceptionController extends ResponseEntityExceptionHandler {

    @ExceptionHandler(DirsLookupException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public void handleWhateverException(DirsLookupException e, HttpServletRequest httpServletRequest) {
        System.out.println("Handling a WhateverException.");
    }


}