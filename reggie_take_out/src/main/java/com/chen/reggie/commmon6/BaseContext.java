package com.chen.reggie.commmon6;

/**
 * 基于ThreadLocal封装的工具类，用于保存和获取当前登录用户id
 * @author chen
 * @create 2022/10/14 15:58
 */
public class BaseContext {

    private static ThreadLocal<Long> threadLocal = new ThreadLocal<>();

    /**
     * 设置id值
     * @param id
     */
    public static void setCurrentId(Long id){
        threadLocal.set(id);
    }

    /**
     * 获取id值
     * @return
     */
    public static Long getCurrentId(){
        return threadLocal.get();
    }


}
