package com.chen.reggie.mapper2;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.chen.reggie.entity1.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户信息
 * @author chen
 * @create 2022/10/21 21:40
 */

@Mapper
public interface UserMapper extends BaseMapper<User> {
}
