package com.example.viewer.models;

import com.example.viewer.enums.ContentType;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ContentModel {
    private ContentType contentType;
    private Object object;
}
