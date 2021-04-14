package com.example.viewer.exceptions;

public class UseFileException extends RuntimeException{
    public UseFileException(String errorMessage, Throwable throwable) {
        super(errorMessage, throwable);
    }

    public UseFileException(String errorMessage) {
        super(errorMessage);
    }
}
