package com.kob.backend.consumer.utils;

import com.kob.backend.utils.JwtUtil;
import io.jsonwebtoken.Claims;

public class JwtAuthentication {
    public static Integer getUserId(String token) {
        String userId = "-1";
        try {
            // 解析token，获取用户ID
            Claims claims = JwtUtil.parseJWT(token);
            userId = claims.getSubject();
        } catch (Exception e) {
            // 解析失败，抛出异常
            throw new RuntimeException("Token parsing error", e);
        }
        return Integer.parseInt(userId);
    }
}
