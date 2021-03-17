package com.example.viewer.util;

import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PathHelper {
    public static String getAbsolutePath(String path){
        String firstChar = path.substring(0, 1);
        return firstChar.equals("/") ? path : "/" + path;
    }
    public static String getRelativePath(String path){
        String firstChar = path.substring(0, 1);
        return firstChar.equals("/") ? path.substring(1) : path;
    }
    public static String skip(String path, int skip){
        String relativePath = getRelativePath(path);
        return Arrays.stream(relativePath.split("/")).skip(skip).collect(Collectors.joining("/"));
    }
    public static String limit(String path, int limit){
        String relativePath = getRelativePath(path);
        return Arrays.stream(relativePath.split("/")).limit(limit).collect(Collectors.joining("/"));
    }
    public static String skipAndLimit(String path, int skip, int limit){
        String relativePath = getRelativePath(path);
        String skipAndLimitedString = Arrays.stream(relativePath.split("/")).skip(skip).limit(limit).collect(Collectors.joining("/"));
        return skipAndLimitedString.equals("") ? "/" : skipAndLimitedString;
    }

}
