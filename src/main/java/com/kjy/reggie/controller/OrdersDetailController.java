package com.kjy.reggie.controller;

import com.kjy.reggie.common.R;
import com.kjy.reggie.domain.Orders;
import com.kjy.reggie.service.OrderDetailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author:柯佳元
 * @version:2.7
 */
@Slf4j
@RestController
@RequestMapping("/orderDetail")
public class OrdersDetailController {


    @Resource
    private OrderDetailService orderDetailService;


    
}
