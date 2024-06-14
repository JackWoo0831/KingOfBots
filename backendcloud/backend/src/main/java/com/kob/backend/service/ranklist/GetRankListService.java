package com.kob.backend.service.ranklist;

import com.alibaba.fastjson2.JSONObject;

public interface GetRankListService {
    JSONObject getRankList(Integer page);
}
