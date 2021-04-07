package com.example.viewer.exceptions;

public class DirsLookupException extends RuntimeException {
    public DirsLookupException(String errorMessage, Throwable throwable) {
        super(errorMessage, throwable);
    }

    public DirsLookupException(String errorMessage) {
        super(errorMessage);
    }
}
