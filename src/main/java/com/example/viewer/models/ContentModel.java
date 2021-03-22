package com.example.viewer.models;

import com.example.viewer.enums.ContentType;

public class ContentModel {
    private ContentType contentType;
    private Object object;

    public ContentModel(ContentType contentType, Object object) {
        this.contentType = contentType;
        this.object = object;
    }

    public ContentModel() {
    }

    public ContentType getContentType() {
        return contentType;
    }

    public void setContentType(ContentType contentType) {
        this.contentType = contentType;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }
}
