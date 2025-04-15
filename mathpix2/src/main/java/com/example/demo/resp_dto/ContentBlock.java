package com.example.demo.resp_dto;

public class ContentBlock {
    private String type;
    private String value;

    public ContentBlock() {}

    public ContentBlock(String type, String value) {
        this.type = type;
        this.value = value;
    }

    // Getters and Setters
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}

