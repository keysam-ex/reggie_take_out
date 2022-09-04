package com.kjy.reggie.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLIntegrityConstraintViolationException;

/**
 * @author:柯佳元
 * @version:2.7
 */
@Slf4j
@ControllerAdvice(annotations = {RestController.class, Controller.class})
@ResponseBody
public class GlobalException {

    /**
     * 异常处理  此方法处理 添加重复 id 导致异常
     *
     * @param ex
     * @return
     */
    @ExceptionHandler(value = {SQLIntegrityConstraintViolationException.class})
    public R<String> exceptionHandler(SQLIntegrityConstraintViolationException ex) {
        log.error("异常信息={}", ex.getMessage());
        if ("#23000".equals(ex.getMessage())) {
            return R.error("名称相同无法添加!");
        }
        return R.error("未知错误!");
    }


    /**
     * 异常处理  此方法处理 删除 菜品分类 如果有 菜品 无法删除 异常
     *
     * @param ex
     * @return
     */
    @ExceptionHandler(value = {CustomException.class})
    public R<String> exceptionHandler(CustomException ex) {
        log.error("异常信息={}", ex.getMessage());
        return R.error(ex.getMessage());
    }
}
