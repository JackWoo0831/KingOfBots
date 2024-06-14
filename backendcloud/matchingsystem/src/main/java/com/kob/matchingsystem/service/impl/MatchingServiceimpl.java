package com.kob.matchingsystem.service.impl;

import com.kob.matchingsystem.service.MatchingService;
import com.kob.matchingsystem.service.impl.util.MatchingPool;
import org.springframework.stereotype.Service;

@Service
public class MatchingServiceimpl implements MatchingService {
    // 在SpringBoot启动前启动
    public final static MatchingPool matchingPool = new MatchingPool();


    @Override
    public String addPlayer(Integer userId, Integer rating, Integer botId) {
        System.out.println("add player: " + userId.toString());
        matchingPool.addPlayer(userId, rating, botId);
        return "add player success";
    }

    @Override
    public String removePlayer(Integer userId) {
        System.out.println("remove player: " + userId.toString());
        matchingPool.removePlayer(userId);
        return "remove player success";
    }
}
