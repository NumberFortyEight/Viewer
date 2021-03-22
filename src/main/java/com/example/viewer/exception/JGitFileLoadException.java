package com.example.viewer.exception;

public class JGitFileLoadException extends Exception {
    public JGitFileLoadException(String errorMessage, Throwable throwable) {
        super(errorMessage, throwable);
    }

    public JGitFileLoadException(String errorMessage) {
        super(errorMessage);
    }
}