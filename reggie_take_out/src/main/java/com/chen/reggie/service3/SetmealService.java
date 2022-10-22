package com.chen.reggie.service3;

import com.baomidou.mybatisplus.extension.service.IService;
import com.chen.reggie.dto.SetmealDto;
import com.chen.reggie.entity1.Setmeal;

import java.util.List;

/**
 * @author chen
 * @create 2022/10/14 23:26
 */
public interface SetmealService extends IService<Setmeal> {

    /**
     * 新增套餐，同时保存套餐和菜品的关联关系
     * @param setmealDto
     */
    public void saveWithDish(SetmealDto setmealDto);

    /**
     * 删除套餐，同时删除套餐和菜品的关联关系
     * @param ids
     */
    public void removeWithDish(List<Long> ids);

}
