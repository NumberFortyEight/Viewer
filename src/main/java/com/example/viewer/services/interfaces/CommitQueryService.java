package com.example.viewer.services.interfaces;

import io.reflectoring.diffparser.api.model.Diff;

import java.io.OutputStream;
import java.util.List;

public interface CommitQueryService {
    String getDiff(String fullPath, String query);
}
