package com.kjy.reggie.common;

/**
 * @author:柯佳元
 * @version:2.7
 * 基于 ThreadLocal封装工具类,用于保持和获取当前用户登录id
 */
public class BaseContext {
    private static ThreadLocal<Long> threadLocal = new ThreadLocal<>();

    /**
     * 保持数据
     * @param id
     */
    public static void setCurrentId(Long id){
        threadLocal.set(id);
    }

    /**
     * 获取数据
     * @return
     */
    public static Long getCurrentId(){
        return threadLocal.get();
    }
}
