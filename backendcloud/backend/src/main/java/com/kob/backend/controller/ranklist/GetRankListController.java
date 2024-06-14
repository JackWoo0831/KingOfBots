package com.kob.backend.controller.ranklist;

import com.alibaba.fastjson2.JSONObject;
import com.kob.backend.service.ranklist.GetRankListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@CrossOrigin
public class GetRankListController {
    @Autowired
    public GetRankListService getRankListService;

    @GetMapping("/ranklist/getlist/")
    public JSONObject getRankList (@RequestParam Map<String, String> data){
        // 分页 和record类似

        Integer page = Integer.parseInt(data.get("page"));
        return getRankListService.getRankList(page);
    }
}
