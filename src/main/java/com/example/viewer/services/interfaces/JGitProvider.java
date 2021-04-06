package com.example.viewer.services.interfaces;

import org.eclipse.jgit.api.Git;

public interface JGitProvider {
    Git getConnection(String fullPath);
}
