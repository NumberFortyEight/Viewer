package com.example.viewer.models;

import com.example.viewer.enums.ContentType;
import lombok.Data;

@Data
public class ContentModel {
    private ContentType contentType;
    private Object object;
}
