package com.kjy.reggie.dto;


import com.kjy.reggie.domain.Dish;
import com.kjy.reggie.domain.DishFlavor;
import lombok.Data;
import java.util.ArrayList;
import java.util.List;

/**
 * 继承了父类的所有属性,并且拓展 了 可以接收到  flavors
 */
@Data
public class DishDto extends Dish {

    private List<DishFlavor> flavors = new ArrayList<>();

    private String categoryName;

    private Integer copies;


}
