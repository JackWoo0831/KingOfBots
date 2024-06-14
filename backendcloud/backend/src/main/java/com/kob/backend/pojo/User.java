package com.kob.backend.pojo;

/*
* Pojo层 负责将mysql中表的内容翻译成hava class
* */

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @TableId(type = IdType.AUTO)  // 按照id 递增提取
    public Integer id;  // 最好不要直接用int 用Integer对象 否则会报warning
    public String username;
    public String password;
    public String photo;
    public Integer rating;
}
