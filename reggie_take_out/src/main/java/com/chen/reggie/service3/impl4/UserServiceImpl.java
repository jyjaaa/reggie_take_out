package com.chen.reggie.service3.impl4;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chen.reggie.commmon6.R;
import com.chen.reggie.entity1.User;
import com.chen.reggie.mapper2.UserMapper;
import com.chen.reggie.service3.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 用户信息
 * @author chen
 * @create 2022/10/21 21:41
 */

@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {


}
