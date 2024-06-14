package com.kob.botrunningsystem.service.impl.util;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

// 生产者-消费者模型 一直执行池中的代码
public class BotPool extends Thread{
    // 定义锁
    private final static ReentrantLock lock = new ReentrantLock();
    // 条件变量 池不空就一直执行 否则就不执行
    private final Condition condition = lock.newCondition();

    private final Queue<Bot> bots = new LinkedList<>();

    public void addBot(Integer userId, String botCode, String input) {
        lock.lock();
        try {
            bots.add(new Bot(userId, botCode, input));
            condition.signalAll();  // 唤醒进程
        } finally {
            lock.unlock();
        }
    }


    private void consume (Bot bot) {
        Consumer consumer = new Consumer();
        consumer.startTimeOut(2000, bot);  // 每个bot 都开新线程

    }

    @Override
    public void run() {
        while (true) {
            lock.lock();

            if (bots.isEmpty()) {
                try {
                    condition.await();  // 默认会unlock
                } catch (InterruptedException e) {
                    lock.unlock();
                    throw new RuntimeException(e);

                }
            }
            else {
                Bot bot = bots.remove();  // 返回队头
                lock.unlock();
                consume(bot);  // 放到解锁后面 因为这个执行比较费时间
            }
        }
    }
}
