package com.example.viewer.models;

import com.example.viewer.enums.State;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FileModel {
    private State state;
    private String name;
    private String href;
}
