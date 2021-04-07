package com.example.viewer.exceptions;

public class JGitException extends RuntimeException{
    public JGitException(String errorMessage, Throwable throwable) {
        super(errorMessage, throwable);
    }

    public JGitException(String errorMessage) {
        super(errorMessage);
    }
}
