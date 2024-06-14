package com.kob.backend.service.pk;

public interface ReceiveBotMove {
    // 负责接收 bot 模块的指定的输入
    String receiveBotMove (Integer userId, Integer direction);
}
