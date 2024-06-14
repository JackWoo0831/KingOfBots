package com.kob.backend.service.impl.user.account;

import com.kob.backend.pojo.User;
import com.kob.backend.service.impl.utils.UserDetailImpl;
import com.kob.backend.service.user.account.InfoService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.HashMap;
import java.util.Map;

@Service
public class InfoServiceImpl implements InfoService {


    @Override
    public Map<String, String> getInfo() {
        // 和LoginService相似
        UsernamePasswordAuthenticationToken authentication =
                (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();


        UserDetailImpl loginUser = (UserDetailImpl) authentication.getPrincipal();  // 需要类型转换

        User user = loginUser.getUser();
        Map<String, String> map = new HashMap<>();
        map.put("error_message", "success");
        map.put("userid", user.getId().toString());
        map.put("username", user.getUsername());
        map.put("password", user.getPassword());
        map.put("photo", user.getPhoto());

        return map;

    }
}
