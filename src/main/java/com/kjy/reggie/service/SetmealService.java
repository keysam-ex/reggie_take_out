package com.kjy.reggie.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kjy.reggie.common.R;
import com.kjy.reggie.domain.Setmeal;
import com.baomidou.mybatisplus.extension.service.IService;
import com.kjy.reggie.domain.vo.AllPage;
import com.kjy.reggie.dto.SetmealDto;

import java.util.List;

/**
* @author 柯佳元
* @description 针对表【setmeal(套餐)】的数据库操作Service
* @createDate 2022-08-27 20:25:09
*/
public interface SetmealService extends IService<Setmeal> {

    /**
     * 新增套餐,同时需要保持套餐和菜品的关联关系
     * @param setmealDto
     */
    public void saveWithDish(SetmealDto setmealDto);


    //此方法用来删除 套餐 和菜品对应关系
    public void deleteWithDish(List<Long> ids);

    public void updateWithSetmealDish(SetmealDto setmealDto);

}
