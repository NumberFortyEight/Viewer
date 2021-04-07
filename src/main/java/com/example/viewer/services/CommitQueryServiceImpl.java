package com.example.viewer.services;

import com.example.viewer.services.interfaces.CommitQueryService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CommitQueryServiceImpl implements CommitQueryService {

    @Override
    public String getDiff(String fullPath, String query) {
        String[] split = query.split("=");
        if (split.length > 1) {
            return null;
        }
        return null;
    }
}
