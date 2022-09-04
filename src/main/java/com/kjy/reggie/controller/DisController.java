package com.kjy.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kjy.reggie.common.CustomException;
import com.kjy.reggie.common.R;
import com.kjy.reggie.domain.Category;
import com.kjy.reggie.domain.Dish;
import com.kjy.reggie.domain.DishFlavor;
import com.kjy.reggie.domain.vo.AllPage;
import com.kjy.reggie.dto.DishDto;
import com.kjy.reggie.service.CategoryService;
import com.kjy.reggie.service.DishFlavorService;
import com.kjy.reggie.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author:柯佳元
 * @version:2.7
 */
@Slf4j
@RestController
@RequestMapping("/dish")
public class DisController {

    @Autowired
    private DishService dishService;

    @Autowired
    private DishFlavorService dishFlavorService;

    @Autowired
    private CategoryService categoryService;


    @PostMapping
    public R<String> save(@RequestBody DishDto dishDto) {

        dishService.saveWithFlavor(dishDto);


        return R.success("添加成功");


    }

    /**
     * 菜品信息分页
     *
     * @param allPage
     * @return
     */
    @GetMapping("/page")
    public R<Page> page(AllPage allPage) {
        //构造分页构造器对象
        Page<Dish> dishPage = new Page<>(allPage.getPage(), allPage.getPageSize());
        Page<DishDto> dishDtoPage = new Page<>();

        //条件构造器
        LambdaQueryWrapper<Dish> dishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        //添加过滤条件 和 排序条件
        dishLambdaQueryWrapper.like(Dish::getName, allPage.getName()).orderByDesc(Dish::getUpdateTime);

        //执行分页查询
        Page<Dish> page = dishService.page(dishPage, dishLambdaQueryWrapper);

        //对象拷贝
        BeanUtils.copyProperties(dishPage, dishDtoPage, "records");


        List<Dish> records = page.getRecords();

        List<DishDto> list = records.stream().map(item -> {
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(item, dishDto);

            Long categoryId = item.getCategoryId();
            //根据id查到 菜品种类名
            Category category = categoryService.getById(categoryId);
            if (category != null) {
                String name = category.getName();
                dishDto.setCategoryName(name);
            }

            return dishDto;
        }).collect(Collectors.toList());

        dishDtoPage.setRecords(list);
        return R.success(dishDtoPage);

    }

    /**
     * 回显菜品信息
     *
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public R<DishDto> updateDish(@PathVariable Long id) {
        DishDto dishDto = dishService.getDishWithFlavor(id);
        return R.success(dishDto);

    }

    /**
     * 更新菜品信息
     *
     * @return
     */
    @PutMapping
    public R<String> update(@RequestBody DishDto dishDto) {
        dishService.updateWithFlavor(dishDto);
        return R.success("更新成功");

    }


    //停售/或则起售
    @PostMapping("/status/{status}")
    public R<String> updateStatus(@PathVariable Integer status, String ids) {
        String[] split = ids.split(",");
        List<Long> collect = Stream.of(split).map(Long::parseLong).collect(Collectors.toList());

        log.info("修改的状态为={},修改菜品的编号为={}", status, ids);
        List<Dish> dishes = dishService.listByIds(collect);
        dishes = dishes.stream().map(item -> {
            item.setStatus(status);
            return item;
        }).collect(Collectors.toList());
        dishService.updateBatchById(dishes);


        return R.success("菜品状态修改成功");
    }

    //删除菜肴
    @DeleteMapping
    public R<String> deleteDish(@RequestParam List<Long> ids) {
        log.info("ids={}", ids);
        LambdaQueryWrapper<Dish> dishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        dishLambdaQueryWrapper.in(Dish::getId, ids).eq(Dish::getStatus, 1);
        int count = dishService.count(dishLambdaQueryWrapper);  //查询所有的  ids 看看有没有 一个以上处于禁止售卖状态 有就无法删除
        if (count > 0) {
            throw new CustomException("菜品存在起售状态,无法删除!");
        }
        dishService.removeByIds(ids);
        return R.success("删除成功!!");

    }


    ///**
    // * 根据条件查询对应的菜品数据
    // * @param dish
    // * @return
    // */
    //@GetMapping("/list")
    //public R<List<Dish>> getDishList(Dish dish){
    //    LambdaQueryWrapper<Dish> dishLambdaQueryWrapper = new LambdaQueryWrapper<>();
    //    dishLambdaQueryWrapper.eq(dish != null && dish.getCategoryId() != null,Dish::getCategoryId,dish.getCategoryId()).ne(Dish::getStatus,0)
    //                            .orderByAsc(Dish::getSort).orderByDesc(Dish::getUpdateTime);
    //    List<Dish> dishList = dishService.list(dishLambdaQueryWrapper);
    //    return  R.success(dishList);
    //
    //}

    /**
     * 根据条件查询对应的菜品数据 兼容 前后台
     * 让前提可以有规格
     * @param dish
     * @return
     */
    @GetMapping("/list")
    public R<List<DishDto>> getDishList(Dish dish) {
        LambdaQueryWrapper<Dish> dishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        //菜品种类对应的 所有菜品 并且不能是 禁售 状态
        dishLambdaQueryWrapper.eq(dish != null && dish.getCategoryId() != null, Dish::getCategoryId, dish.getCategoryId()).ne(Dish::getStatus, 0)
                .orderByAsc(Dish::getSort).orderByDesc(Dish::getUpdateTime);
        List<Dish> dishList = dishService.list(dishLambdaQueryWrapper);

        List<DishDto> dishDtos = dishList.stream().map(item -> {
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(item, dishDto);
            Long id = item.getCategoryId();
            Category category = categoryService.getById(id);
            if (category != null) {
                String name = category.getName();
                dishDto.setCategoryName(name);
            }

            LambdaQueryWrapper<DishFlavor> dishFlavorLambdaQueryWrapper = new LambdaQueryWrapper<>();
            dishFlavorLambdaQueryWrapper.eq(DishFlavor::getDishId, item.getId());
            List<DishFlavor> dishFlavorList = dishFlavorService.list(dishFlavorLambdaQueryWrapper);
            dishDto.setFlavors(dishFlavorList);
            return dishDto;
        }).collect(Collectors.toList());

        return R.success(dishDtos);

    }


}
