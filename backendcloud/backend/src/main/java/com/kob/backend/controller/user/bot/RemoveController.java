package com.kob.backend.controller.user.bot;

import com.kob.backend.service.user.bot.RemoveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@CrossOrigin
public class RemoveController {

    @Autowired
    RemoveService removeService;

    @PostMapping("/user/bot/remove/") // 修改数据库 都是post
    public Map<String, String> removeBot (@RequestParam Map<String, String> map) {
        return removeService.remove(map);
    }
}
