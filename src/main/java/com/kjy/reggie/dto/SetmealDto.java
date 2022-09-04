package com.kjy.reggie.dto;

import com.kjy.reggie.domain.Setmeal;
import com.kjy.reggie.domain.SetmealDish;
import lombok.Data;

import java.util.List;


@Data
public class SetmealDto extends Setmeal {

    //套餐所关联到的所有菜品
    private List<SetmealDish> setmealDishes;

    //套餐的类型
    private String categoryName;
}
