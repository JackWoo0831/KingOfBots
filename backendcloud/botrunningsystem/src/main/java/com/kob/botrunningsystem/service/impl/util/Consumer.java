package com.kob.botrunningsystem.service.impl.util;

import com.kob.botrunningsystem.utils.BotInterface;
import org.joor.Reflect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;

@Component  // 为了使用springboot间通信的rest template
public class Consumer extends Thread {
    private Bot bot;
    public static RestTemplate restTemplate;

    @Autowired
    public void setRestTemplate(RestTemplate restTemplate) {
        Consumer.restTemplate = restTemplate;
    }

    private static final String receiveBotMoveUrl = "http://localhost:3000/pk/receive/bot/move/";

    public void startTimeOut (long timeout, Bot bot) {
        this.bot = bot;

        this.start();

        try {
            /*
            * Waits at most millis milliseconds for this thread to terminate.
            * A timeout of 0 means to wait forever. This method returns immediately,
            * without waiting, if the thread has not been started.
            * */
            this.join(timeout);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            this.interrupt();  // 超时就终止这个线程
        }
    }

    private String addUid(String code, String uid) {  // 在code中的Bot类名后添加uid
        int k = code.indexOf(" implements com.kob.botrunningsystem.utils.BotInterface");
        return code.substring(0, k) + uid + code.substring(k);
    }


    @Override
    public void run() {
        // 编译string形式的java代码

        // Reflect compile对于同一个类名只编译一次 因此加上一个随机字符串
        UUID uuid = UUID.randomUUID();
        String uid = uuid.toString().substring(0, 8);


        BotInterface botInterface = Reflect.compile(
            "com.kob.botrunningsystem.utils.BotInterfaceImpl" + uid,
                addUid(bot.getBotCode(), uid)
        ).create().get();

        Integer direction = botInterface.nextMove(bot.getInput());

        System.out.println(bot.getUserId() + direction.toString());

        MultiValueMap<String, String> data = new LinkedMultiValueMap<>();
        data.add("user_id", bot.getUserId().toString());
        data.add("direction", direction.toString());

        restTemplate.postForObject(receiveBotMoveUrl, data, String.class);
    }
}
