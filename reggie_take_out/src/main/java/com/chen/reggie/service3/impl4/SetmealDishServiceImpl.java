package com.chen.reggie.service3.impl4;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chen.reggie.entity1.SetmealDish;
import com.chen.reggie.mapper2.SetmealDishMapper;
import com.chen.reggie.service3.SetmealDishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 *
 * 以Setmeal为操作的主表，所以创建 SetmealController，不用 SetmealDishController
 * @author chen
 * @create 2022/10/19 18:18
 */

@Service
@Slf4j
public class SetmealDishServiceImpl extends ServiceImpl<SetmealDishMapper, SetmealDish> implements SetmealDishService {
}
