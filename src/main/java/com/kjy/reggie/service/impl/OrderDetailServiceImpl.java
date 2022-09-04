package com.kjy.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kjy.reggie.domain.OrderDetail;
import com.kjy.reggie.service.OrderDetailService;
import com.kjy.reggie.mapper.OrderDetailMapper;
import org.springframework.stereotype.Service;

/**
* @author 柯佳元
* @description 针对表【order_detail(订单明细表)】的数据库操作Service实现
* @createDate 2022-09-01 12:13:47
*/
@Service
public class OrderDetailServiceImpl extends ServiceImpl<OrderDetailMapper, OrderDetail>
    implements OrderDetailService{

}




