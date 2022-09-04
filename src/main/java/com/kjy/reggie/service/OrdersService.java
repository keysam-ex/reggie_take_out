package com.kjy.reggie.service;

import com.kjy.reggie.domain.Orders;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author 柯佳元
* @description 针对表【orders(订单表)】的数据库操作Service
* @createDate 2022-09-01 12:13:43
*/
public interface OrdersService extends IService<Orders> {

    /**
     * 用户下单
     * @param orders
     */
    public void submit(Orders orders);

}
