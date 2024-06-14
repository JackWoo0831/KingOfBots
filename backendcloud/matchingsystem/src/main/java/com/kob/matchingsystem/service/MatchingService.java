package com.kob.matchingsystem.service;

public interface MatchingService {
    // 匹配系统 实现 往匹配池中添加或删除玩家
    String addPlayer(Integer userId, Integer rating, Integer botId);
    String removePlayer(Integer userId);
}
