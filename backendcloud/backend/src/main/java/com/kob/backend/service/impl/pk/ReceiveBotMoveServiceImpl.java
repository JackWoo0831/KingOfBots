package com.kob.backend.service.impl.pk;

import com.kob.backend.consumer.WebSocketServer;
import com.kob.backend.consumer.utils.Game;
import com.kob.backend.service.pk.ReceiveBotMove;
import org.springframework.stereotype.Service;

@Service
public class ReceiveBotMoveServiceImpl implements ReceiveBotMove {
    @Override
    public String receiveBotMove(Integer userId, Integer direction) {
        System.out.println("receiveBotMove " + userId + " " + direction);

        if (WebSocketServer.users.get(userId) != null) {
            Game game = WebSocketServer.users.get(userId).game;  // 把user对应的game取出来
            if (game != null) {
                // 看看是谁的操作
                if (game.getPlayerA().getId().equals(userId)) {
                    //
                    game.setNextStepA(direction);

                }
                else if (game.getPlayerB().getId().equals(userId)) {
                    game.setNextStepB(direction);
                }
            }

        }


        return "receive bot move success";
    }
}
