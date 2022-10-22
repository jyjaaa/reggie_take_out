package com.chen.reggie.commmon6;

/**
 * 自定义业务异常类
 * @author chen
 * @create 2022/10/15 0:06
 */

public class CustomException extends RuntimeException {
    public CustomException(String message){
        super(message);
    }
}
