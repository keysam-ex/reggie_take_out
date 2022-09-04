package com.kjy.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kjy.reggie.domain.ShoppingCart;
import com.kjy.reggie.service.ShoppingCartService;
import com.kjy.reggie.mapper.ShoppingCartMapper;
import org.springframework.stereotype.Service;

/**
* @author 柯佳元
* @description 针对表【shopping_cart(购物车)】的数据库操作Service实现
* @createDate 2022-08-29 22:28:45
*/
@Service
public class ShoppingCartServiceImpl extends ServiceImpl<ShoppingCartMapper, ShoppingCart>
    implements ShoppingCartService{

}




