package com.kob.backend.consumer.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// 表示蛇的身体
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Cell {
    int x;
    int y;
}
