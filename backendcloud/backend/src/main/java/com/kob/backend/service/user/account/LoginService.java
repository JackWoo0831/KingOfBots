package com.kob.backend.service.user.account;

import java.util.Map;

// 这里是接口 在service/impl里面会有对应的实现
public interface LoginService {
    Map<String, String> login(String username, String password);  // 接口默认是public 不是private
}
