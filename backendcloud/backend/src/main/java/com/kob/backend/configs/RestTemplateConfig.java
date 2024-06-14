package com.kob.backend.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {
    // 实现springboot间的server通信

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
