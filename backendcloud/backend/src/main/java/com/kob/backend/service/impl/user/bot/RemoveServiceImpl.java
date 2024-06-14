package com.kob.backend.service.impl.user.bot;

import com.kob.backend.mapper.BotMapper;
import com.kob.backend.pojo.Bot;
import com.kob.backend.pojo.User;
import com.kob.backend.service.impl.utils.UserDetailImpl;
import com.kob.backend.service.user.bot.RemoveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class RemoveServiceImpl implements RemoveService {
    @Autowired
    BotMapper botMapper;  // 数据库接口注入

    @Override
    public Map<String, String> remove(Map<String, String> data) {

        // 取出用户 看看要删除的bot是不是该用户的
        UsernamePasswordAuthenticationToken token =
                (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();

        UserDetailImpl login_user = (UserDetailImpl) token.getPrincipal();
        User user = login_user.getUser();


        int bot_id = Integer.parseInt(data.get("bot_id"));

        Bot bot = botMapper.selectById(bot_id);

        Map<String, String> res = new HashMap<String, String>();

        if (bot == null) {
            res.put("error_message", "bot does not exist");
            return res;
        }

        if (!bot.getUserId().equals(user.getId())) {
            res.put("error_message", "Permission denied");
            return res;
        }

        botMapper.deleteById(bot_id);
        res.put("error_message", "success");

        return res;
    }
}
