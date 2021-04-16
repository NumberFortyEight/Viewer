package com.example.viewer.controllers;

import com.example.viewer.dataClasses.Content;
import com.example.viewer.dataClasses.Node;
import com.example.viewer.services.interfaces.JGitFacadeService;
import com.example.viewer.services.MainQueryService;
import com.example.viewer.util.PathHelper;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@CrossOrigin
@RequiredArgsConstructor
public class MainController {

    private final Map<String, Node> userAndNodeTree = new HashMap<>();
    public final MainQueryService mainQueryService;
    public final JGitFacadeService jgitFacadeService;

    @GetMapping("/committree")
    public Map<String, Node> getUserAndNodeTree(){
        return userAndNodeTree;
    }

    @GetMapping("api/{student}/{repository}/**")
    @ResponseStatus(HttpStatus.OK)
    public Object mainJGitApi(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String user = "One";
        String fullPath = PathHelper.getFullPath(request.getRequestURI());
        Optional<String> OptionalQuery = Optional.ofNullable(request.getQueryString());
        OptionalQuery.ifPresent(query -> mainQueryService.queryLogic(user, fullPath, query, userAndNodeTree));

        Content content = jgitFacadeService.getContent(user, fullPath, userAndNodeTree);
        switch (content.getContentType()){
            case IMAGE:
                response.setContentType(MediaType.IMAGE_JPEG_VALUE);
                IOUtils.copy(new ByteArrayInputStream((byte[]) content.getObject()), response.getOutputStream());
                return null;
            case VIDEO:
                response.setContentType("video/mp4");
                IOUtils.copy(new ByteArrayInputStream((byte[]) content.getObject()), response.getOutputStream());
                return null;
            default:
                return content.getObject();
        }
    }
}
