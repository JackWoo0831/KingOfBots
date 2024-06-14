package com.kob.backend.service.impl.user.account;


import com.kob.backend.pojo.User;
import com.kob.backend.service.impl.utils.UserDetailImpl;
import com.kob.backend.service.user.account.LoginService;
import com.kob.backend.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;


@Service
public class LoginServiceImpl implements LoginService {

    @Autowired
    private AuthenticationManager authenticationManager;  // springboot自带的 验证模块

    @Override
    public Map<String, String> login(String username, String password) {

        // 根据用户名密码创建token
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(username, password);

        // 根据token查看授权 如果用户名没密码不对 这个就会直接报错
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);

        UserDetailImpl loginUser = (UserDetailImpl) authenticate.getPrincipal();  // 需要类型转换
        User user = loginUser.getUser();

        // 形成jwt token
        String jwt = JwtUtil.createJWT(user.getId().toString());

        // 构造返回值
        Map<String, String> map = new HashMap<>();
        map.put("error_message", "success");
        map.put("token", jwt);

        return map;
    }
}
