package com.kob.backend.service.user.bot;


import java.util.Map;

// 实现对bot table 的增删改查 为了便于调试 分开写
public interface AddService {
    // 返回值一般都写map 返回信息
    Map<String, String> add (Map<String, String> data);
}
