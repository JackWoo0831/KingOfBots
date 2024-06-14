package com.kob.backend.service.impl.user.bot;

import com.kob.backend.mapper.BotMapper;
import com.kob.backend.pojo.Bot;
import com.kob.backend.pojo.User;
import com.kob.backend.service.impl.utils.UserDetailImpl;
import com.kob.backend.service.user.bot.UpdateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class UpdateServiceImpl implements UpdateService {
    @Autowired
    BotMapper botMapper;

    @Override
    public Map<String, String> update(Map<String, String> data) {
        UsernamePasswordAuthenticationToken token =
                (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();

        UserDetailImpl login_user = (UserDetailImpl) token.getPrincipal();

        User user = login_user.getUser();

        int bot_id = Integer.parseInt(data.get("bot_id"));
        // 可以改变的column title description content

        // 检查是否为作者
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

        // 检查修改的内容是否合法
        // 需要传title description content三样信息
        String title = data.get("title");
        String content = data.get("content");
        String description = data.get("description");

        Map<String, String> map = new HashMap<String, String>();

        if (title == null || title.isEmpty()) {
            map.put("error_message", "title is empty");
            return map;
        }

        if (title.length() > 100) {
            map.put("error_message", "title is longer than 100 characters");
            return map;
        }

        if (content == null || content.isEmpty()) {
            map.put("error_message", "content is empty");
            return map;

        }

        if (description == null || description.isEmpty()) {
            description = "the user too lazy to leave there empty";
        }

        if (description.length() > 300) {
            map.put("error_message", "description is longer than 300 characters");
            return map;
        }

        Date now = new Date();
        Bot new_bot = new Bot(bot.getId(), user.getId(), title,
                description, content, bot.getCreatetime(), now);
        botMapper.updateById(new_bot);

        map.put("error_message", "success");

        return map;
    }
}
