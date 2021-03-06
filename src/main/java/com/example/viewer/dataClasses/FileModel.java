package com.example.viewer.dataClasses;

import com.example.viewer.dataClasses.enums.State;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FileModel {
    private State state;
    private String name;
    private Integer commitTime;
    private String href;
}