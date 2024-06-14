package com.kob.botrunningsystem.service.impl.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Bot {
    // Bot类 在pool中的元素
    Integer userId;
    String botCode;
    String input;  // 当前的局面
}
