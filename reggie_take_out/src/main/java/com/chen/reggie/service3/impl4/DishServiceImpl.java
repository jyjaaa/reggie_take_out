package com.chen.reggie.service3.impl4;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chen.reggie.dto.DishDto;
import com.chen.reggie.entity1.Dish;
import com.chen.reggie.entity1.DishFlavor;
import com.chen.reggie.mapper2.DishMapper;
import com.chen.reggie.service3.DishFlavorService;
import com.chen.reggie.service3.DishService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author chen
 * @create 2022/10/14 23:26
 */
@Service
@Slf4j
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {

    @Autowired
    private DishFlavorService dishFlavorService;

    @Autowired
    private DishMapper dishMapper;//1

    /**
     * 新增菜品，同时保存对应的口味数据
     *
     * @param dishDto
     * @Transactional: 同一方法下多表操作时加事务控制，启动类上加开启事务
     */
    @Transactional
    @Override
    public void saveWithFlavor(DishDto dishDto) {

        //保存菜品的基本信息到菜品表dish
        this.save(dishDto);

        Long dishId = dishDto.getId(); //菜品id

        //菜品口味
        List<DishFlavor> flavors = dishDto.getFlavors();

        //遍历(两种遍历方式)，每一个item再放入dishId
        // 将菜品Id添加到flavors中 在转换成list,成为一个新的flavors集合,
        flavors = flavors.stream().map((item) -> {
            item.setDishId(dishId);
            return item;
        }).collect(Collectors.toList());

        dishFlavorService.saveBatch(flavors);

//        保存菜品的基本信息到菜品表dish
//        Dish dish = new Dish();
//        dish.setName(dishDto.getName());
//        dish.setCategoryId(dishDto.getCategoryId());
//        dish.setPrice(dishDto.getPrice());
//        dish.setCode(dishDto.getCode());
//        dish.setImage(dishDto.getImage());
//        dish.setDescription(dishDto.getDescription());
//        dish.setStatus(DishConstant.STATUS_KS);

//        this.save(dish);
//        Long dishId = dish.getId();

        //        flavors.forEach(i->{
//           item.setDishId(dishId);
//        });
        //保存菜品口味数据到菜品口味表dishflavor,saveBatch:批量添加

//        List<DishFlavor> flavors = new ArrayList<>();

        //forEach循环
//        flavors.forEach(dto -> {
//            DishFlavor dishFlavor = new DishFlavor();
//            dishFlavor.setDishId(dishId);
//            dishFlavor.setName(dto.getName());
//           flavors.add(dishFlavor);
//        dishFlavorService.save(dishFlavor);
//        });
//
//        //基本for循环
//        for (int i = 0; flavors.size() > 0; i++) {
//            DishFlavor dishFlavor = new DishFlavor();
//            dishFlavor.setDishId(dishId);
//            dishFlavor.setName(flavors.get(i).getName());
//            ...
//            flavors.add(dishFlavor);
//        }
//        //增强for循环
//        for(DishFlavor dto: flavors){
//            DishFlavor dishFlavor = new DishFlavor();
//            dishFlavor.setDishId(dishId);
//            dishFlavor.setName(dto.getName());
//            ....
//        flavors.add(dishFlavor);
//        }

//        dishFlavorService.saveBatch(flavors);


    }

    /**
     * 根据id查询菜品信息和对应的口味信息
     *
     * @param id
     * @return
     */
    @Override
    public DishDto getByIdWithFlavor(Long id) {
        //查询菜品基本信息，从dish表查询
        Dish dish = getById(id);


        DishDto dishDto = new DishDto();
        BeanUtils.copyProperties(dish, dishDto);



//        if (dish != null) {
//
//        }
//        Dish dd = null;
//        dd.getId();
//        Dish d=new Dish();
//        Long qq = d.getId();
//        Long id1 = d.getId();


        //查询当前菜品口味信息，从dish_flavor表查询
        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
//        Wrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotEmpty(dish.getId() + "")) {
            queryWrapper.eq(DishFlavor::getDishId, dish.getId());
        }
        List<DishFlavor> flavors = dishFlavorService.list(queryWrapper);
        dishDto.setFlavors(flavors);

        return dishDto;
    }

    /**
     * 修改菜品
     * @param dishDto
     */
    @Override
    @Transactional
    public void updateWithFlavor(DishDto dishDto) {
        //更新dish表
        this.updateById(dishDto);

        //清除当前菜品对应的口味数据：delete---dishflavor表
        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DishFlavor::getDishId, dishDto.getId());
        dishFlavorService.remove(queryWrapper);

        //添加当前提交过来的口味数据：insert---dishflavor表
        List<DishFlavor> flavors = dishDto.getFlavors();
        flavors = flavors.stream().map((item) -> {
            item.setDishId(dishDto.getId());
            return item;
        }).collect(Collectors.toList());

        dishFlavorService.saveBatch(flavors);


    }
}
