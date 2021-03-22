package com.example.viewer.models;

import com.example.viewer.enums.State;

public class FileModel {

    private State state;

    private String name;

    private String href;

    public FileModel(State state, String name, String href) {
        this.state = state;
        this.name = name;
        this.href = href;
    }

    public FileModel() {
    }

    public static class FileModelBuilder{
        private FileModel newFileModel;

        public FileModelBuilder(){
            newFileModel = new FileModel();
        }
        public FileModelBuilder withState(State state){
            newFileModel.state = state;
            return this;
        }
        public FileModelBuilder withName(String name){
            newFileModel.name = name;
            return this;
        }
        public FileModelBuilder withHref(String href){
            newFileModel.href = href;
            return this;
        }
        public FileModel build(){
            return newFileModel;
        }
    }

    public State getState() {
        return state;
    }

    public String getName() {
        return name;
    }

    public String getHref() {
        return href;
    }

    @Override
    public String toString() {
        return "FileModel{" +
                "state=" + state +
                ", name='" + name + '\'' +
                ", href='" + href + '\'' +
                '}';
    }
}
