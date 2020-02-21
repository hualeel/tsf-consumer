package com.hua.tsf_spring_cloud_demo.tsf_spring_cloud_consumer.Entity;

//用户自定义的metadata
public class CustomMetadata {
    private String name;
    private String value;

    public CustomMetadata(String name, String value) {
        this.name = name;
        this.value = value;
    }
}
