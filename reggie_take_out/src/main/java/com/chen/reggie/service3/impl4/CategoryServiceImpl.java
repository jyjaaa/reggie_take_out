package com.chen.reggie.service3.impl4;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chen.reggie.commmon6.CustomException;
import com.chen.reggie.entity1.Category;
import com.chen.reggie.entity1.Dish;
import com.chen.reggie.entity1.Setmeal;
import com.chen.reggie.mapper2.CategoryMapper;
import com.chen.reggie.service3.CategoryService;
import com.chen.reggie.service3.DishService;
import com.chen.reggie.service3.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author chen
 * @create 2022/10/14 19:47
 */

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper,Category> implements CategoryService{

    @Autowired
    private DishService dishService;

    @Autowired
    private SetmealService setmealService;

    /**
     * 根据id删除分类，删除之前需要进行判断
     * @param id
     */
    @Override
    public void remove(Long id) {

        //添加查询条件，根据分类id进行查询 eq(key,value)
        LambdaQueryWrapper<Dish> dishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        dishLambdaQueryWrapper.eq(Dish::getCategoryId,id);
        int count1 = dishService.count(dishLambdaQueryWrapper);
        //查询当前分类是否关联了菜品，如果已经关联，抛出一个业务异常
        if (count1 > 0){
            //已经关联菜品，抛出一个业务异常
            throw new CustomException("当前分类关联了菜品,不能删除");
        }

        //查询当前分类是否关联了套餐，如果已经关联，抛出一个业务异常
        LambdaQueryWrapper<Setmeal> SetmealLambdaQueryWrapper = new LambdaQueryWrapper<>();
        SetmealLambdaQueryWrapper.eq(Setmeal::getCategoryId,id);
        setmealService.count(SetmealLambdaQueryWrapper);
        int count2 =setmealService.count(SetmealLambdaQueryWrapper);

        if (count2 > 0){
            //已经关联菜品，抛出一个业务异常
            throw new CustomException("当前分类关联了套餐,不能删除");
        }
        //正常删除分类
        super.removeById(id);
    }
}
