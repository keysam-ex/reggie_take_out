package com.kjy.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kjy.reggie.domain.DishFlavor;
import com.kjy.reggie.service.DishFlavorService;
import com.kjy.reggie.mapper.DishFlavorMapper;
import org.springframework.stereotype.Service;

/**
* @author 柯佳元
* @description 针对表【dish_flavor(菜品口味关系表)】的数据库操作Service实现
* @createDate 2022-08-27 23:46:58
*/
@Service
public class DishFlavorServiceImpl extends ServiceImpl<DishFlavorMapper, DishFlavor>
    implements DishFlavorService{

}




