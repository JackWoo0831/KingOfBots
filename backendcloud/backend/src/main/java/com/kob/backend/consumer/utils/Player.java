package com.kob.backend.consumer.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Player {
    // 记录每个玩家的id 起始坐标 走过的路径等
    private Integer id;
    private Integer botId;  // -1 人工操作
    private String botCode;
    private Integer sx;  // 起始坐标 对应row
    private Integer sy;
    private List<Integer> steps;  // 走过的路

    private boolean check_tail_increasing(int step) {  // 检验当前回合，蛇的长度是否增加
        if (step <= 10) return true;
        return step % 3 == 1;
    }


    // 直接计算 当前蛇的身体有哪些
    public List<Cell> getCells () {
        List<Cell> res = new ArrayList<>();

        int[] dx = {-1, 1, 0, 0}, dy = {0, 0, -1, 1};
        int x = sx, y = sy;
        int step = 0;
        res.add(new Cell(x, y));
        // 按照走过的轨迹 模拟出来
        for (int d: steps) {
            x += dx[d];
            y += dy[d];
            res.add(new Cell(x, y));
            if (!check_tail_increasing( ++ step)) {
                res.removeFirst();  // 不增长 就把尾巴移除
            }
        }
        return res;

    }


    public String getStepsString() {
        StringBuilder res = new StringBuilder();
        for (int d: steps) {
            res.append(d);
        }
        return res.toString();
    }

}
