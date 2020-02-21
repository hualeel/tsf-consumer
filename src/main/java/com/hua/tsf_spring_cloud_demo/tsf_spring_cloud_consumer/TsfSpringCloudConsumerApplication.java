package com.hua.tsf_spring_cloud_demo.tsf_spring_cloud_consumer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.tsf.annotation.EnableTsf;
import org.springframework.tsf.auth.annotation.EnableTsfAuth;
import org.springframework.web.client.AsyncRestTemplate;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableFeignClients // 使用 Feign 微服务调用时请启用
@EnableTsf
//服务鉴权
@EnableTsfAuth
public class TsfSpringCloudConsumerApplication {

    @LoadBalanced
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @LoadBalanced
    @Bean
    public AsyncRestTemplate asyncRestTemplate() {
        return new AsyncRestTemplate();
    }


    public static void main(String[] args) {
        SpringApplication.run(TsfSpringCloudConsumerApplication.class, args);
    }

}
