package com.kob.backend.controller.user.account;

import com.kob.backend.service.user.account.InfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@CrossOrigin  // 解决跨域问题
public class InfoController {
    @Autowired
    private InfoService infoService;

    @GetMapping("/user/account/info/")
    public Map<String, String> info() {
        return infoService.getInfo();
    }

    /* 这部分流程和LoginController差不多 注入infoService 然后直接返回即可
    前端调试代码:
    $.ajax({
        url: "http://localhost:3000/user/account/info/",
        type: 'get',
        // header协议参见 com/kob/backend/configs/filter/JwtAuthenticationTokenFilter.java
        headers: {
          Authorization: "Bearer " + "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiI1MWU4NWI3MjEwNDE0YTA2YTVhNWZhNTYyODU1NDMwOCIsInN1YiI6IjAiLCJpc3MiOiJzZyIsImlhdCI6MTcxNzQwMzExOCwiZXhwIjoxNzE4NjEyNzE4fQ.Kr7kCwsWYd97u2PkBT7AbsBDik9-P7s9AV_UtAqexiM"
        }
      })
    *
    *
    * */
}
