package com.example.viewer.services;

import com.example.viewer.services.interfaces.CommitQueryService;
import com.example.viewer.services.jgit.JGitCommitInfo;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CommitQueryServiceImpl implements CommitQueryService {
    private final ApplicationContext appContext;

    @Override
    public String getDiff(String fullPath, String query) {
        String[] split = query.split("=");
        if (split.length > 1) {
            return appContext.getBean(JGitCommitInfo.class, fullPath).getDiffOrNull(Integer.parseInt(split[1]));
        }
        return null;
    }
}
