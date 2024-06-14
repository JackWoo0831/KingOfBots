package com.kob.backend.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;


// 实现匹配系统 需要用web socket通信 这是因为http通信是立即返回请求的 但是匹配系统需要等待匹配算法
// websocket是全双工的 这样服务器可以跟client发信息
@Configuration
public class WebSocketConfig {

    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }
}
