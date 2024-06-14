package com.kob.backend.controller.user.bot;

import com.kob.backend.service.user.bot.UpdateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@CrossOrigin
public class UpdateController {

    @Autowired
    UpdateService updateService;

    @PostMapping("/user/bot/update/")
    public Map<String, String> updateBot(@RequestParam Map<String, String> request) {
        return updateService.update(request);
    }
}
