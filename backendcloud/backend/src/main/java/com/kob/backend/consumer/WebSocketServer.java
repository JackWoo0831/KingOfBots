package com.kob.backend.consumer;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.kob.backend.consumer.utils.Game;
import com.kob.backend.consumer.utils.JwtAuthentication;
import com.kob.backend.mapper.BotMapper;
import com.kob.backend.mapper.RecordMapper;
import com.kob.backend.mapper.UserMapper;
import com.kob.backend.pojo.Bot;
import com.kob.backend.pojo.User;
import jakarta.websocket.*;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.MultiValueMapAdapter;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
@ServerEndpoint("/websocket/{token}")  // 不要以/结尾
public class WebSocketServer {

    private Session session = null;  // 每个链接都用session维护
    private User user;  // 和user对应上
    // 将user id和token结合起来 这样知道匹配之后该给谁发信息
    // static 是因为 所有实例都共享 Concurrent是为了线程安全
    // public是因为game类中也需要发送信息
    public static ConcurrentHashMap<Integer, WebSocketServer> users = new ConcurrentHashMap<>();

    // 匹配池 是User的list
    // private static final CopyOnWriteArrayList<User> matchPool = new CopyOnWriteArrayList<>();

    // usermapper 也是共享的
    public static UserMapper userMapper;

    // 多线程的游戏过程
    public Game game = null;

    public static RecordMapper recordMapper;

    // Bot数据库的Mapper 因为要根据Bot id把bot 的代码取出来
    private static BotMapper botMapper;


    @Autowired
    public void setUserMapper(UserMapper userMapper) {
        WebSocketServer.userMapper = userMapper;
    }

    @Autowired
    public void setRecordMapper(RecordMapper recordMapper) {
        WebSocketServer.recordMapper = recordMapper;
    }

    @Autowired
    public void setBotMapper(BotMapper botMapper) { WebSocketServer.botMapper = botMapper; }

    // Springboot 间 通信
    public static RestTemplate restTemplate;

    @Autowired
    public void setRestTemplate(RestTemplate restTemplate) {
        WebSocketServer.restTemplate = restTemplate;
    }

    // 通信到matching system的url
    private static final String addPlayerUrl = "http://localhost:3030/player/add/";
    private static final String removePlayerUrl = "http://localhost:3030/player/remove/";


    @OnOpen
    public void onOpen(Session session, @PathParam("token") String token) throws IOException {
        // 开启链接
        this.session = session;
        System.out.println("connected to websocket");

        Integer userId = JwtAuthentication.getUserId(token);

        this.user = userMapper.selectById(userId);

        if (this.user != null) {
            users.put(userId, this);  // 存下来
        }
        else {
            this.session.close();
        }

    }

    @OnClose
    public void onClose() {
        // 关闭链接
        System.out.println("disconnected from websocket");
        if (this.user != null) {
            users.remove(this.user.getId());
        }
    }



    // 得到用户id 后 开始游戏
    public static void startGame (Integer aId, Integer aBotId, Integer bId, Integer bBotId) {
        User a = userMapper.selectById(aId);
        User b = userMapper.selectById(bId);

        // 取Bot
        Bot botA = botMapper.selectById(aBotId);
        Bot botB = botMapper.selectById(bBotId);

        // 给前端发消息
        // 给匹配成功的A B 各自发
        JSONObject respA = new JSONObject();
        JSONObject respB = new JSONObject();

        // 在这里实例化一个地图 两个用户是一样的
        Game game = new Game(
                13,
                14,
                20,
                a.getId(),
                b.getId(),
                botA,
                botB
                );

        game.createMap();
        users.get(a.getId()).game = game;  // 匹配的两个用户的game是当前创造的game
        users.get(b.getId()).game = game;

        game.start();  // game的运行过程必须是多线程的 因为一次有好多用户组匹配


        // 把地图信息封装成json 在前端显示
        JSONObject respGame = new JSONObject();
        respGame.put("a_id", game.getPlayerA().getId());
        respGame.put("a_sx", game.getPlayerA().getSx());
        respGame.put("a_sy", game.getPlayerA().getSy());

        respGame.put("b_id", game.getPlayerB().getId());
        respGame.put("b_sx", game.getPlayerB().getSx());
        respGame.put("b_sy", game.getPlayerB().getSy());

        respGame.put("map", game.getG());

        respA.put("event", "match-finished");
        respA.put("opponent_username", b.getUsername());
        respA.put("opponent_photo", b.getPhoto());
        respA.put("game", respGame);

        respB.put("event", "match-finished");
        respB.put("opponent_username", a.getUsername());
        respB.put("opponent_photo", a.getPhoto());
        respB.put("game", respGame);

        users.get(a.getId()).sendMessage(respA.toJSONString());
        users.get(b.getId()).sendMessage(respB.toJSONString());


    }

    private void startMatching (Integer botId) {
        System.out.println("start matching");

        // 往 matching server发请求

        MultiValueMap<String, String> data = new LinkedMultiValueMap<>();
        data.add("user_id", user.getId().toString());
        data.add("rating", user.getRating().toString());
        data.add("bot_id", botId.toString());

        // 访问请求
        restTemplate.postForObject(addPlayerUrl, data, String.class);
    }

    private void stopMatching () {
        System.out.println("stop matching");

        MultiValueMap<String, String> data = new LinkedMultiValueMap<>();
        data.add("user_id", user.getId().toString());

        // 访问请求
        restTemplate.postForObject(removePlayerUrl, data, String.class);
    }

    public void move (int direction) {
        // 判断一下该给谁发 牢记 一个session就会实例化一个socket类
        if (game.getPlayerA().getId().equals(user.getId())) {
            // 当人工操作的时候 才设置方向 否则会干扰bot
            if (game.getPlayerA().getBotId().equals(-1)) {
                game.setNextStepA(direction);
            }

        }
        else if (game.getPlayerB().getId().equals(user.getId())) {
            if (game.getPlayerB().getBotId().equals(-1)) {
                game.setNextStepB(direction);
            }
        }
    }

    @OnMessage
    public void onMessage(String message) {
        // 从前端接收信息
        System.out.println("received message: " + message);

        JSONObject data = JSON.parseObject(message);
        String event = data.getString("event");
        if (event != null && event.equals("start-matching")) {
            this.startMatching(data.getInteger("bot_id"));
        }
        else if (event != null && event.equals("stop-matching")) {
            this.stopMatching();
        }
        else if (event != null && event.equals("move")) {
            this.move(data.getInteger("direction"));
        }
    }

    @OnError
    public void onError(Session session, Throwable t) {

    }

    public void sendMessage(String message) {
        // 后端给前端发消息
        synchronized (this.session) {
            try {
                this.session.getBasicRemote().sendText(message);
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


}
