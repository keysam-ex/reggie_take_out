package com.kjy.reggie.common;

import com.alibaba.fastjson.JSON;

/**
 * @author:柯佳元
 * @version:2.7
 * 自定义异常,用来处理删除菜品种类
 */
public class CustomException extends RuntimeException {


    public CustomException(String message) {

        super(message);
    }
}
