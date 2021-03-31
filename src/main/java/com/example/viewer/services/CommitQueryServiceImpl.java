package com.example.viewer.services;

import com.example.viewer.services.interfaces.CommitQueryService;
import com.example.viewer.services.jgit.JGitScope;
import io.reflectoring.diffparser.api.model.Diff;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.List;

@Service
public class CommitQueryServiceImpl implements CommitQueryService {

    @Override
    public String getDiff(String fullPath, String query) {
        String[] split = query.split("=");
        if (split.length > 1) {
            return new JGitScope(fullPath).getCommitInfo().getDiffOrNull(Integer.parseInt(split[1]));
        }
        return null;
    }
}
