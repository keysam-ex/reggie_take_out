package com.kjy.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kjy.reggie.common.CustomException;
import com.kjy.reggie.common.R;
import com.kjy.reggie.domain.Category;
import com.kjy.reggie.domain.Setmeal;
import com.kjy.reggie.domain.SetmealDish;
import com.kjy.reggie.domain.vo.AllPage;
import com.kjy.reggie.dto.SetmealDto;
import com.kjy.reggie.service.SetmealDishService;
import com.kjy.reggie.service.SetmealService;
import com.kjy.reggie.mapper.SetmealMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author 柯佳元
 * @description 针对表【setmeal(套餐)】的数据库操作Service实现
 * @createDate 2022-08-27 20:25:09
 */
@Service
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal>
        implements SetmealService {

    @Autowired
    private SetmealDishService setmealDishService;

    /**
     * 新增套餐 同时需要保持套餐和菜品的关联
     *
     * @param setmealDto
     */
    @Override
    @Transactional
    public void saveWithDish(SetmealDto setmealDto) {
        //保持套餐的基本信息 操作  SetmealServiceImpl 添加
        this.save(setmealDto);
        //保持套餐和菜品的关联信息 操作  SetmealDishService 操作
        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
        setmealDishes = setmealDishes.stream().map(item -> {
            item.setSetmealId(setmealDto.getId());
            return item;
        }).collect(Collectors.toList());
        setmealDishService.saveBatch(setmealDishes);
    }

    @Override
    @Transactional
    public void deleteWithDish(List<Long> ids) {

        //判断ids是否存在启售状态,存在就抛出异常，不然就删除套餐
        LambdaQueryWrapper<Setmeal> setmealLambdaQueryWrapper = new LambdaQueryWrapper<>();
        setmealLambdaQueryWrapper.in(Setmeal::getId, ids).eq(Setmeal::getStatus, 1);
        int count = this.count(setmealLambdaQueryWrapper);

        if (count > 0) {
            throw new CustomException("套餐存在起售状态,无法删除!");
        }


        //如果可以删除，先删除套餐中的数据 setmeal
        this.removeByIds(ids);
        //删除关系表中的数据 setmeal_dish
        LambdaQueryWrapper<SetmealDish> setmealDishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        setmealDishLambdaQueryWrapper.in(SetmealDish::getSetmealId, ids);
        setmealDishService.remove(setmealDishLambdaQueryWrapper);
    }

    @Override
    public void updateWithSetmealDish(SetmealDto setmealDto) {
        //更新套餐的所有信息
        this.updateById(setmealDto);


        List<SetmealDish> setmealDishList = setmealDto.getSetmealDishes();
        setmealDishList =  setmealDishList.stream().map(item ->{
            item.setSetmealId(setmealDto.getId());
            return item;
        }).collect(Collectors.toList());

        //删除套餐菜肴关系信息
        Long id = setmealDto.getId();
        LambdaQueryWrapper<SetmealDish> setmealDishLambdaUpdateWrapper = new LambdaQueryWrapper<>();
        setmealDishLambdaUpdateWrapper.eq(SetmealDish::getSetmealId, id);
        setmealDishService.remove(setmealDishLambdaUpdateWrapper);

        //重新添加信息
        setmealDishService.saveBatch(setmealDishList);


    }


}




