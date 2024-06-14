package com.kob.backend.controller.user.bot;

import com.kob.backend.pojo.Bot;
import com.kob.backend.service.impl.user.bot.GetListServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin
public class GetListController {
    @Autowired
    GetListServiceImpl getListService;

    @GetMapping("/user/bot/getlist/")
    public List<Bot> getBotList() {
        return getListService.getList();
    }
}
