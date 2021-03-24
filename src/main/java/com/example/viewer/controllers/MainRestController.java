package com.example.viewer.controllers;

import com.example.viewer.models.CommitModel;
import com.example.viewer.models.ContentModel;
import com.example.viewer.models.Node;
import com.example.viewer.services.JGitService;
import com.example.viewer.services.NodeTreeService;
import com.example.viewer.services.QueryService;
import com.example.viewer.util.PathHelper;
import com.example.viewer.services.jgit.JGitCommitInfo;
import com.example.viewer.services.jgit.JGitScope;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@ResponseBody
@CrossOrigin
public class MainRestController {

    private final Map<String, Node> userAndNodeTree = new HashMap<>();
    public final NodeTreeService nodeTreeService;
    public final QueryService queryService;
    public final JGitService jgitService;

    public MainRestController(NodeTreeService nodeTreeService, QueryService queryService, JGitService jgitService) {
        this.nodeTreeService = nodeTreeService;
        this.queryService = queryService;
        this.jgitService = jgitService;
    }

    @GetMapping("/{student}/{repository}/allCommits")
    public List<CommitModel> doGetAllCommits(HttpServletRequest request){
        String fullPath = PathHelper.limit(request.getRequestURI(),2);
        return new JGitScope(fullPath).getInfo().getCommitModelList();
    }

    @GetMapping("/committree")
    public Map<String, Node> getUserAndNodeTree(){
        return userAndNodeTree;
    }

    @GetMapping("/{student}/{repository}/**")
    @ResponseStatus(HttpStatus.OK)
    public Object doGetObject(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String user = "One";
        String fullPath = URLDecoder.decode(request.getRequestURI(), Charset.defaultCharset());
        Optional<String> OptionalQuery = Optional.ofNullable(request.getQueryString());
        OptionalQuery.ifPresent(query -> queryService.queryLogic(user, fullPath, query, userAndNodeTree));

        ContentModel contentModel = jgitService.getContent(user, fullPath, userAndNodeTree);
        switch (contentModel.getContentType()){
            case IMAGE:
                response.setContentType(MediaType.IMAGE_JPEG_VALUE);
                IOUtils.copy(new ByteArrayInputStream((byte[]) contentModel.getObject()), response.getOutputStream());
                return null;
            case VIDEO:
                response.setContentType("video/mp4");
                IOUtils.copy(new ByteArrayInputStream((byte[]) contentModel.getObject()), response.getOutputStream());
                return null;
            default:
                return contentModel.getObject();
        }
    }
}
