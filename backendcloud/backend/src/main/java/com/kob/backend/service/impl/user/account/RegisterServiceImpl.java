package com.kob.backend.service.impl.user.account;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kob.backend.mapper.UserMapper;
import com.kob.backend.pojo.User;
import com.kob.backend.service.user.account.RegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RegisterServiceImpl implements RegisterService {

    @Autowired
    UserMapper userMapper;  // 用于查询是否重复

    @Autowired
    private PasswordEncoder passwordEncoder;


    @Override
    public Map<String, String> register(String username, String password, String confirmPassword) {
        Map<String, String> map = new HashMap<String, String>();
        if (username == null) {
            map.put("error_message", "username is null");
            return map;
        }
        else if (password == null) {
            map.put("error_message", "password is null");
            return map;
        }

        username = username.trim();
        if (username.isEmpty()) {
            map.put("error_message", "username is empty");
            return map;
        }
        if (password.isEmpty() || confirmPassword.isEmpty()) {
            map.put("error_message", "password or confirm password is empty");
            return map;
        }

        if (username.length() > 100) {
            map.put("error_message", "username too long");
            return map;
        }

        if (password.length() > 100) {
            map.put("error_message", "password too long");
            return map;
        }

        if (!password.equals(confirmPassword)) {
            map.put("error_message", "passwords not match");
            return map;
        }

        // 查询是否已经注册
        QueryWrapper<User> wrapper = new QueryWrapper<User>();
        wrapper.eq("username", username);
        List<User> users = userMapper.selectList(wrapper);
        if (!users.isEmpty()) {
            map.put("error_message", "user already exist");
            return map;
        }

        // 都合法 给密码加密
        String password_encoded = passwordEncoder.encode(password);
        String photo = "https://cdn.acwing.com/media/user/profile/photo/269415_lg_b804ad0ca4.png";
        User user = new User(null, username, password_encoded, photo, 1500);
        userMapper.insert(user);

        map.put("success_message", "success");
        return map;

    }
}
