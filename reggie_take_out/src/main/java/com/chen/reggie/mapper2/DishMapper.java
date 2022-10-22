package com.chen.reggie.mapper2;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.chen.reggie.entity1.Dish;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author chen
 * @create 2022/10/14 23:19
 */

@Mapper
public interface DishMapper extends BaseMapper<Dish> {

}
