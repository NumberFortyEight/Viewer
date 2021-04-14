package com.example.viewer.exceptions.JGit;

public class JGitCommitInfoException extends RuntimeException {
    public JGitCommitInfoException(String errorMessage, Throwable throwable) {
        super(errorMessage, throwable);
    }

    public JGitCommitInfoException(String errorMessage) {
        super(errorMessage);
    }
}
