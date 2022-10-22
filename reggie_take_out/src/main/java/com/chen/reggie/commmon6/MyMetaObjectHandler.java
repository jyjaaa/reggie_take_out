package com.chen.reggie.commmon6;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * 自定义元数据（Meta）对象处理器
 * @author chen
 * @create 2022/10/14 14:18
 */

@Component
@Slf4j
public class MyMetaObjectHandler implements MetaObjectHandler {

    /**
     * 插入操作时，自动填充
     * @param metaObject
     */
    @Override
    public void insertFill(MetaObject metaObject) {

        log.info("公共字段自动填充[insert]...");
        log.info(metaObject.toString());
        metaObject.setValue("createTime", LocalDateTime.now());
        metaObject.setValue("updateTime", LocalDateTime.now());

        //获取当前登录用户id值
        metaObject.setValue("createUser", BaseContext.getCurrentId());
        metaObject.setValue("updateUser", BaseContext.getCurrentId());

    }

    /**
     * 更新操作时，自动填充
     * @param metaObject
     */
    @Override
    public void updateFill(MetaObject metaObject) {

        log.info("公共字段自动填充[update]...");
        log.info(metaObject.toString());

//        //获得当前线程用户id（执行插入更新操作时会自动更新id）
//        long id = Thread.currentThread().getId();
//        log.info("线程id为：{}",id);

        //获取当前登录用户id值
        metaObject.setValue("updateTime", LocalDateTime.now());
        metaObject.setValue("updateUser", BaseContext.getCurrentId());
    }
}
