package com.example.viewer.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@RestController
@ResponseBody
public class MainRestController {

    @GetMapping("{student}/{repository}/**")
    @ResponseStatus(HttpStatus.OK)
    public Object jgitRestApi(HttpServletRequest request){
        String user = "One";
        String fullPath = request.getRequestURI();
        Optional<String> OptionalQuery = Optional.of(request.getQueryString());
        return null;

    }

}
