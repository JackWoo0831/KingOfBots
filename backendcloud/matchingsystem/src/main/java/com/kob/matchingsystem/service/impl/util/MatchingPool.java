package com.kob.matchingsystem.service.impl.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.locks.ReentrantLock;

@Component  // 为了能够注入让springboot之间通信的 RestTemplate
public class MatchingPool extends Thread {
    /*
    * 匹配的逻辑 每隔1s 扫描匹配池
    * 一开始 只匹配分数很相近的 随着时间的推移 分值的允许误差越来越大
    * */

    // 存所有用户
    private static List<Player> players = new ArrayList<Player>();  // 多个线程公用
    // 处理读写冲突

    // 定义锁
    private ReentrantLock lock = new ReentrantLock();

    // springboot间通信的rest template
    private static RestTemplate restTemplate;

    // 注入进来 固定搭配
    @Autowired
    public void setRestTemplate(RestTemplate restTemplate) {
        MatchingPool.restTemplate = restTemplate;
    }

    // 发送的url
    private final static String startGameUrl = "http://localhost:3000/pk/start/game/";

    public void addPlayer (Integer userId, Integer rating, Integer botId) {
        lock.lock();
        try {
            players.add(new Player(userId, rating, 0, botId));
        }
        finally {
            lock.unlock();
        }
    }

    public void removePlayer (Integer userId) {
        lock.lock();
        try {
            // 把不删的放进来 就行
            List<Player> players_backup = new ArrayList<>();
            for (Player player : players) {
                if (!player.getUserId().equals(userId)) {
                    players_backup.add(player);
                }
            }

            players = players_backup;
        }
        finally {
            lock.unlock();
        }
    }

    private void increaseWaitingTime () {
        // 所有玩家waiting time + 1
        lock.lock();
        try {
            for (Player player : players) {
                player.setWaitingTime(player.getWaitingTime() + 1);
            }
        }
        finally {
            lock.unlock();
        }
    }

    private void matchPlayers () {
        // 尝试匹配
        lock.lock();
        try {

            boolean[] used = new boolean[players.size()];
            for (int i = 0; i < players.size(); i ++ ) {
                if (used[i]) continue;
                for (int j = i + 1; j < players.size(); j ++ ) {
                    if (used[j]) continue;
                    Player a = players.get(i), b = players.get(j);
                    if (checkMatched(a, b)) {
                        used[i] = used[j] = true;
                        sendResult(a, b);
                        break;
                    }
                }
            }

            List<Player> newPlayers = new ArrayList<>();
            for (int i = 0; i < players.size(); i ++ ) {
                if (!used[i]) {
                    newPlayers.add(players.get(i));
                }
            }
            players = newPlayers;

        }
        finally {
            lock.unlock();
        }
    }

    private boolean checkMatched (Player a, Player b) {
        // 不能自己和自己匹配
        if (Objects.equals(a.getUserId(), b.getUserId()))
            return false;
        // 判断两个玩家是否匹配
        int ratingDelta = Math.abs(a.getRating() - b.getRating());
        int waitingTime = Math.min(a.getWaitingTime(), b.getWaitingTime());
        return ratingDelta <= waitingTime * 10;

    }

    private void sendResult (Player a, Player b) {
        // 发送结果

        MultiValueMap<String, String> data = new LinkedMultiValueMap<String, String>();

        data.add("a_id", a.getUserId().toString());
        data.add("a_bot_id", a.getBotId().toString());

        data.add("b_id", b.getUserId().toString());
        data.add("b_bot_id", b.getBotId().toString());

        System.out.println("paired: " + data.toString());

        restTemplate.postForObject(startGameUrl, data, String.class);
    }

    @Override
    public void run() {
        // 死循环 每隔1s 检查一下 并更新玩家的等待时间
        while (true) {
            try {
                Thread.sleep(1000);
                lock.lock();
                try {
                    increaseWaitingTime();
                    matchPlayers();
                } finally {
                    lock.unlock();
                }

            } catch (InterruptedException e) {
                e.printStackTrace();
                break;
            }
        }

    }
}
