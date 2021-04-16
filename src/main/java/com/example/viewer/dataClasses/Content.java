package com.example.viewer.dataClasses;

import com.example.viewer.dataClasses.enums.ContentType;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Content {
    private ContentType contentType;
    private Object object;
}
