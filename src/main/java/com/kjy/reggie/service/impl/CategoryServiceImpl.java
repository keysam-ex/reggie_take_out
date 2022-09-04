package com.kjy.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kjy.reggie.common.CustomException;
import com.kjy.reggie.domain.Category;
import com.kjy.reggie.domain.Dish;
import com.kjy.reggie.domain.Setmeal;
import com.kjy.reggie.service.CategoryService;
import com.kjy.reggie.mapper.CategoryMapper;
import com.kjy.reggie.service.DishService;
import com.kjy.reggie.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
* @author 柯佳元
* @description 针对表【category(菜品及套餐分类)】的数据库操作Service实现
* @createDate 2022-08-27 19:35:55
*/
@Service
@Slf4j
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category>
    implements CategoryService{

    @Autowired
    private DishService dishService;
    @Autowired
    private SetmealService setmealService;

    @Override
    public void remove(Long id) {

        log.info("要删除菜品类的id={}",id);
        LambdaQueryWrapper<Dish> dishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        //添加查询条件,根据分类 id 进行查询
        dishLambdaQueryWrapper.eq(Dish::getCategoryId,id);
        int count1 = dishService.count(dishLambdaQueryWrapper);

        //查询当前分类是否关联到菜品,如果已经关联，抛出一个业务异常
        if (count1 > 0){
            //已经关联到菜品，抛出一个异常
            throw new CustomException("无法删除,当前分类关联到菜品");
        }

        //查询当前分类是否关联到套餐,如果已经关联，抛出一个业务异常
        LambdaQueryWrapper<Setmeal> setmealLambdaQueryWrapper = new LambdaQueryWrapper<>();
        //添加查询条件,根据分类 id 进行查询
        setmealLambdaQueryWrapper.eq(Setmeal::getCategoryId,id);
        int count2 = setmealService.count(setmealLambdaQueryWrapper);

        if (count2 >0){
            //已经关联到套餐，抛出一个异常
            throw new CustomException("无法删除,关联到套餐菜品");
        }

        //正常删除
        super.removeById(id);


    }
}




