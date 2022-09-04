package com.kjy.reggie.service;

import com.kjy.reggie.domain.Category;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author 柯佳元
* @description 针对表【category(菜品及套餐分类)】的数据库操作Service
* @createDate 2022-08-27 19:35:55
*/
public interface CategoryService extends IService<Category> {

    /**
     * 处理 删除 菜品分类 的业务
     * @param id
     */
    public void remove(Long id);

}
