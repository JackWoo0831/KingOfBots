package com.kob.backend.controller.user.bot;

import com.kob.backend.service.user.bot.AddService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/*
*
* #1、建数据库
#2、写pojo
#3、写mapper
#4、写controller
##4.1写service接口
##4.2写impl实现（重点）
##4.3写一个controller
* */

@RestController
@CrossOrigin
public class AddController {

    @Autowired
    private AddService addService;

    @PostMapping("/user/bot/add/")
    public Map<String, String> add (@RequestParam Map<String, String> request) {
        return addService.add(request);
    }

    /* 前端调试代码
    $.ajax({
            url: 'http://localhost:3000/user/bot/add/',
            type: 'post',
            data: {
                title: 'title',
                description: 'description',
                content: 'content',
            },
            headers: {
                  Authorization: "Bearer " + store.state.user.token
            },

            success (resp) {
                console.log(resp)
            },
            error (resp) {
                console.log(resp)
            }

        })

        $.ajax({
            url: 'http://localhost:3000/user/bot/remove/',
            type: 'post',
            data: {
                bot_id: 1
            },
            headers: {
                  Authorization: "Bearer " + store.state.user.token
            },

            success (resp) {
                console.log(resp)
            },
            error (resp) {
                console.log(resp)
            }

        })
        $.ajax({
            url: 'http://localhost:3000/user/bot/update/',
            type: 'post',
            data: {
                bot_id: 1,
                title: 'new_title',
                content: 'new_content',
                description: 'new_description',
            },
            headers: {
                  Authorization: "Bearer " + store.state.user.token
            },

            success (resp) {
                console.log(resp)
            },
            error (resp) {
                console.log(resp)
            }

        })

        $.ajax({
            url: 'http://localhost:3000/user/bot/getlist/',
            type: 'get',
            headers: {
                  Authorization: "Bearer " + store.state.user.token
            },

            success (resp) {
                console.log(resp)
            },
            error (resp) {
                console.log(resp)
            }

        })
    *
    * */
}
