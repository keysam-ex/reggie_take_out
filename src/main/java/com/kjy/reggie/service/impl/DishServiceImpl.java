package com.kjy.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kjy.reggie.common.R;
import com.kjy.reggie.domain.Dish;
import com.kjy.reggie.domain.DishFlavor;
import com.kjy.reggie.domain.vo.AllPage;
import com.kjy.reggie.dto.DishDto;
import com.kjy.reggie.service.CategoryService;
import com.kjy.reggie.service.DishFlavorService;
import com.kjy.reggie.service.DishService;
import com.kjy.reggie.mapper.DishMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author 柯佳元
 * @description 针对表【dish(菜品管理)】的数据库操作Service实现
 * @createDate 2022-08-27 20:25:05
 */
@Service
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish>
        implements DishService {

    @Autowired
    private DishFlavorService dishFlavorService;

    @Autowired
    private CategoryService categoryService;

    @Transactional
    public void saveWithFlavor(DishDto dishDto) {
        //保持菜品的基本信息到菜品表dish
        this.save(dishDto);

        //获得 菜品 id
        Long id = dishDto.getId();

        //菜品口味
        List<DishFlavor> flavors = dishDto.getFlavors();
        flavors = flavors.stream().map(item -> {
            item.setDishId(id);
            return item;
        }).collect(Collectors.toList());

        //保持菜品口味到菜品口味表 dish_flavor
        dishFlavorService.saveBatch(flavors);
    }

    /**
     * 此方法用来回显 修改菜单的数据
     *
     * @param id
     */
    @Override
    public DishDto getDishWithFlavor(Long id) {
        //根据 id 查到 菜肴 的信息
        Dish dish = this.getById(id);
        DishDto dishDto = new DishDto();
        //将 dish的 信息 拷贝 到  dishDto
        BeanUtils.copyProperties(dish, dishDto);
        //通过条件构造器 找到 该菜肴对应 的口味信息
        LambdaQueryWrapper<DishFlavor> dishFlavorLambdaQueryWrapper = new LambdaQueryWrapper<>();
        dishFlavorLambdaQueryWrapper.eq(DishFlavor::getDishId, id);
        List<DishFlavor> dishFlavors = dishFlavorService.list(dishFlavorLambdaQueryWrapper);
        dishDto.setFlavors(dishFlavors);
        return dishDto;


    }

    @Override
    @Transactional
    public void updateWithFlavor(DishDto dishDto) {
        //先更新dish菜品表
        this.updateById(dishDto);
        //清理当前菜品对应口味数据 -- dish_flavor 的 delete 操作
        LambdaQueryWrapper<DishFlavor> dishFlavorLambdaQueryWrapper = new LambdaQueryWrapper<>();
        dishFlavorLambdaQueryWrapper.eq(DishFlavor::getDishId, dishDto.getId());
        dishFlavorService.remove(dishFlavorLambdaQueryWrapper);

        //添加当前提交过来的口味数据 -dish_flavor 表的 insert 操作
        List<DishFlavor> flavors = dishDto.getFlavors();
        flavors = flavors.stream().map(item -> { //将每种口味 关联到对应的菜品id
            item.setDishId(dishDto.getId());
            return item;
        }).collect(Collectors.toList());

        dishFlavorService.saveBatch(flavors);


    }


}




