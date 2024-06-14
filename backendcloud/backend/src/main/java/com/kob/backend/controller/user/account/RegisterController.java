package com.kob.backend.controller.user.account;


import com.kob.backend.service.user.account.RegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class RegisterController {

    @Autowired
    RegisterService registerService;

    @PostMapping("/user/account/register/")  // 密文传输
    public Map<String, String> register(@RequestParam Map<String, String> map) {
        String username = map.get("username");
        String password = map.get("password");
        String confirmPassword = map.get("confirmPassword");
        return registerService.register(username, password, confirmPassword);

        /* 前端调试代码
        $.ajax({
        url: "http://localhost:3000/user/account/register/",
        type: 'post',
        data: {
          username: 'user1',
          password: 'user1',
          confirmPassword: 'user1'
        },
        success (resp) {
          console.log(resp)
        },
        error (resp) {
          console.log(resp)
        }
      })
        * */
    }

}
