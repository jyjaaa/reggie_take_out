package com.chen.reggie.service3;

import com.baomidou.mybatisplus.extension.service.IService;
import com.chen.reggie.dto.DishDto;
import com.chen.reggie.entity1.Dish;

/**
 * @author chen
 * @create 2022/10/14 23:24
 */
public interface DishService extends IService<Dish> {

    //新增菜品，同时插入菜品对应的口味数据，需要操作两张表，dish dishflavor
    public void saveWithFlavor(DishDto dishDto);

    //根据id查询菜品信息和对应的口味信息
    public DishDto getByIdWithFlavor(Long id);

    //更新菜品信息，同时更新口味信息
    public void updateWithFlavor(DishDto dishDto);

}
