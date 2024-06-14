package com.kob.backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kob.backend.pojo.User;
import org.apache.ibatis.annotations.Mapper;

/*
* mapper层 负责将增删改查(crud create read update delete)操作 映射回数据库中
* 用mybatisplus 就不需要自己实现增删改查了
*
* */
@Mapper
public interface UserMapper extends BaseMapper<User> {

}