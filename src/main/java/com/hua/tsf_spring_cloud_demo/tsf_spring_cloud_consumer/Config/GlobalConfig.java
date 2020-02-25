package com.hua.tsf_spring_cloud_demo.tsf_spring_cloud_consumer.Config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

@Component
//开启 refresh 机制
@RefreshScope
//注解来标明这个类是一个配置类
@ConfigurationProperties(prefix = "global.config")
public class GlobalConfig {
    private String name = "test1234";

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
