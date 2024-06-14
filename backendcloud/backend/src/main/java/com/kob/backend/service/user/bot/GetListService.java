package com.kob.backend.service.user.bot;

import com.kob.backend.pojo.Bot;

import java.util.List;

public interface GetListService {
    // 返回一堆bot 用户的信息在token中 因此不需要传参
    List<Bot> getList ();
}
