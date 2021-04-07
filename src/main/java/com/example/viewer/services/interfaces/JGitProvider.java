package com.example.viewer.services.interfaces;

import org.eclipse.jgit.api.Git;

import java.io.IOException;

public interface JGitProvider {
    Git getConnection(String fullPath) throws IOException;
}
