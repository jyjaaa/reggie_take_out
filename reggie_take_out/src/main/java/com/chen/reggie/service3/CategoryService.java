package com.chen.reggie.service3;

import com.baomidou.mybatisplus.extension.service.IService;
import com.chen.reggie.entity1.Category;

/**
 * @author chen
 * @create 2022/10/14 19:47
 */
public interface CategoryService extends IService<Category> {

    public void remove(Long id);
}
