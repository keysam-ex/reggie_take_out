package com.kjy.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.kjy.reggie.common.BaseContext;
import com.kjy.reggie.common.R;
import com.kjy.reggie.domain.ShoppingCart;
import com.kjy.reggie.service.CategoryService;
import com.kjy.reggie.service.ShoppingCartService;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Delete;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author:柯佳元
 * @version:2.7
 */
@RestController
@Slf4j
@RequestMapping("/shoppingCart")
public class ShoppingCartController {

    @Resource
    private ShoppingCartService shoppingCartService;


    @PostMapping("/add")
    public R<ShoppingCart> add(@RequestBody ShoppingCart shoppingCart) {

        log.info("购物车数据={}", shoppingCart);
        //设置用户id 指定当前是哪个用户的购物车数据
        Long currentId = BaseContext.getCurrentId();
        shoppingCart.setUserId(currentId);
        //查询当前菜品或则套餐是否在购物车中
        Long dishId = shoppingCart.getDishId();
        LambdaQueryWrapper<ShoppingCart> shoppingCartLambdaQueryWrapper = new LambdaQueryWrapper<>();
        shoppingCartLambdaQueryWrapper.eq(shoppingCart.getUserId() != null, ShoppingCart::getUserId, currentId);


        if (dishId != null) { //说明是菜品
            shoppingCartLambdaQueryWrapper.eq(ShoppingCart::getDishId, dishId);

        } else { //是套餐
            shoppingCartLambdaQueryWrapper.eq(ShoppingCart::getSetmealId, shoppingCart.getSetmealId());
        }

        ShoppingCart shoppingCar = shoppingCartService.getOne(shoppingCartLambdaQueryWrapper);
        if (shoppingCar != null) {
            //如果已经存在购物车,就在原来数量基础上加一
            shoppingCar.setNumber(shoppingCar.getNumber() + 1);
            shoppingCartService.updateById(shoppingCar);
        } else {
            //设置菜肴的创建时间
            shoppingCart.setCreateTime(LocalDateTime.now());
            //如果不存在,则添加到购物车,数量默认 一
            shoppingCart.setNumber(1);
            shoppingCartService.save(shoppingCart);
            shoppingCar = shoppingCart;
        }


        return R.success(shoppingCar);
    }

    /**
     * 返回所有购物车的信息
     *
     * @return
     */
    @GetMapping("/list")
    public R<List<ShoppingCart>> list() {
        log.info("查看购物车");
        LambdaQueryWrapper<ShoppingCart> shoppingCartLambdaQueryWrapper = new LambdaQueryWrapper<>();
        shoppingCartLambdaQueryWrapper.eq(ShoppingCart::getUserId, BaseContext.getCurrentId())
                .orderByDesc(ShoppingCart::getCreateTime);
        List<ShoppingCart> shoppingCarts = shoppingCartService.list(shoppingCartLambdaQueryWrapper);
        return R.success(shoppingCarts);
    }

    /**
     * 清除购物车
     *
     * @return
     */
    @DeleteMapping("/clean")
    public R<String> cleanCart() {
        LambdaQueryWrapper<ShoppingCart> shoppingCartLambdaQueryWrapper = new LambdaQueryWrapper<>();
        shoppingCartLambdaQueryWrapper.eq(ShoppingCart::getUserId, BaseContext.getCurrentId());
        shoppingCartService.remove(shoppingCartLambdaQueryWrapper);
        return R.success("清除购物车成功!");
    }

    /**
     * 减少购物车
     */
    @PostMapping("/sub")
    public R<ShoppingCart> subCart(@RequestBody ShoppingCart shoppingCart) {
        Long currentId = BaseContext.getCurrentId();//当前用户的id
        shoppingCart.setUserId(currentId);
        Long dishId = shoppingCart.getDishId();

        LambdaQueryWrapper<ShoppingCart> shoppingCartLambdaQueryWrapper = new LambdaQueryWrapper<>();
        shoppingCartLambdaQueryWrapper.eq(ShoppingCart::getUserId, currentId);

        if (dishId != null) { //如果是菜品
            shoppingCartLambdaQueryWrapper.eq(ShoppingCart::getDishId, dishId);
        } else { //如果是套餐
            shoppingCartLambdaQueryWrapper.eq(ShoppingCart::getSetmealId, shoppingCart.getSetmealId());
        }

        ShoppingCart shoppingCartServiceOne = shoppingCartService.getOne(shoppingCartLambdaQueryWrapper);
        Integer number = shoppingCartServiceOne.getNumber();
        if (number > 1) {
        shoppingCartServiceOne.setNumber(shoppingCartServiceOne.getNumber() - 1);
        shoppingCartService.updateById(shoppingCartServiceOne);
        }else {
            shoppingCartServiceOne.setNumber(0);
            shoppingCartService.removeById(shoppingCartServiceOne);
        }

        return R.success(shoppingCartServiceOne);

    }

}
