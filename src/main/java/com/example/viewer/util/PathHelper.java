package com.example.viewer.util;

import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PathHelper {
    public static String getAbsolutePath(String path) {
        if (path != null && !path.equals("")) {
            String firstChar = path.substring(0, 1);
            return firstChar.equals("/") ? path : "/" + path;
        } else {
            return path;
        }
    }

    public static String getRelativePath(String path) {
        if (path != null && !path.equals("")) {
            String firstChar = path.substring(0, 1);
            return firstChar.equals("/") ? path.substring(1) : path;
        } else {
            return path;
        }

    }

    public static String skip(String path, int skip) {
        String relativePath = getRelativePath(path);
        return Arrays.stream(relativePath.split("/")).skip(skip).collect(Collectors.joining("/"));
    }

    public static String limit(String path, int limit) {
        String relativePath = getRelativePath(path);
        return Arrays.stream(relativePath.split("/")).limit(limit).collect(Collectors.joining("/"));
    }

    public static String skipAndLimit(String path, int skip, int limit) {
        String relativePath = getRelativePath(path);
        String skipAndLimitedString = Arrays.stream(relativePath.split("/")).skip(skip).limit(limit).collect(Collectors.joining("/"));
        return skipAndLimitedString.equals("") ? "/" : skipAndLimitedString;
    }

    public static String getFullPath(String requestURI){
        return PathHelper.getAbsolutePath(PathHelper.skip(URLDecoder.decode(requestURI, Charset.defaultCharset()), 1));
    }

}
