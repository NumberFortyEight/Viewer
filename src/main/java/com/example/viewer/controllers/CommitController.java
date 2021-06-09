package com.example.viewer.controllers;

import com.example.viewer.dataClasses.Commit;
import com.example.viewer.services.interfaces.CommitService;
import com.example.viewer.util.PathHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@CrossOrigin
@RestController
@RequiredArgsConstructor
public class CommitController {

    private final CommitService commitService;

    @GetMapping("/commit/{student}/{repository}/allcommits")
    public List<Commit> getAllCommits(HttpServletRequest request, @PathVariable String repository, @PathVariable String student) {
        log.trace("request for all commits of user = '{}' for repo = '{}' ",student,repository);
        return commitService.getAllCommits(PathHelper.getFullPath(request.getRequestURI()));
    }
    @GetMapping("commit/{student}/{repository}/**")
    public List<Commit> getCommitsByPath(@RequestParam(required = false) Integer unixTime, HttpServletRequest request){
        List<Commit> commitsByPath = commitService.getCommitsByPath(PathHelper.getFullPath(request.getRequestURI()));
        if (unixTime == null){
            return commitsByPath;
        } else {
            return commitsByPath.stream()
                    .filter(commit -> Integer.parseInt(commit.getSimpleDateFormat()) <= unixTime)
                    .collect(Collectors.toList());
        }
    }
}
