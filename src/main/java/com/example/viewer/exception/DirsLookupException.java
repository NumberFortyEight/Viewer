package com.example.viewer.exception;

public class DirsLookupException extends RuntimeException {
    public DirsLookupException(String errorMessage, Throwable throwable) {
        super(errorMessage, throwable);
    }

    public DirsLookupException(String errorMessage) {
        super(errorMessage);
    }
}
