package com.kjy.reggie.service;

import com.kjy.reggie.common.R;
import com.kjy.reggie.domain.Dish;
import com.baomidou.mybatisplus.extension.service.IService;
import com.kjy.reggie.dto.DishDto;

/**
* @author 柯佳元
* @description 针对表【dish(菜品管理)】的数据库操作Service
* @createDate 2022-08-27 20:25:05
*/
public interface DishService extends IService<Dish> {

    public void saveWithFlavor(DishDto dishDto) ;

    public DishDto getDishWithFlavor(Long id);

    //更新菜品信息同时更新口味信息
    public void updateWithFlavor(DishDto dishDto);

}
