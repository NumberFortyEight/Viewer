package com.example.viewer.exceptions.JGit;

import com.example.viewer.exceptions.JGit.JGitException;

public class JGitAddRepositoryException extends JGitException {

    public JGitAddRepositoryException(String errorMessage, Throwable throwable) {
        super(errorMessage, throwable);
    }

    public JGitAddRepositoryException(String errorMessage) {
        super(errorMessage);
    }
}
