package com.example.viewer.controllers;

import com.example.viewer.models.ContentModel;
import com.example.viewer.models.Node;
import com.example.viewer.services.CreationOrUpdateNodeTreeService;
import com.example.viewer.services.MainQueryService;
import com.example.viewer.services.interfaces.JGitService;
import com.example.viewer.util.PathHelper;
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
import java.util.Map;
import java.util.Optional;

@RestController
@CrossOrigin
public class RestMainController {

    private final Map<String, Node> userAndNodeTree = new HashMap<>();
    public final CreationOrUpdateNodeTreeService creationOrUpdateNodeTreeService;
    public final MainQueryService mainQueryService;
    public final JGitService jgitService;

    public RestMainController(CreationOrUpdateNodeTreeService creationOrUpdateNodeTreeService, MainQueryService mainQueryService, JGitService jgitService) {
        this.creationOrUpdateNodeTreeService = creationOrUpdateNodeTreeService;
        this.mainQueryService = mainQueryService;
        this.jgitService = jgitService;
    }

    @GetMapping("/committree")
    public Map<String, Node> getUserAndNodeTree(){
        return userAndNodeTree;
    }

    @GetMapping("api/{student}/{repository}/**")
    @ResponseStatus(HttpStatus.OK)
    public Object mainJGitApi(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String user = "One";
        String fullPath = PathHelper.getAbsolutePath(PathHelper.skip(URLDecoder.decode(request.getRequestURI(), Charset.defaultCharset()), 1));
        Optional<String> OptionalQuery = Optional.ofNullable(request.getQueryString());
        OptionalQuery.ifPresent(query -> mainQueryService.queryLogic(user, fullPath, query, userAndNodeTree));

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
