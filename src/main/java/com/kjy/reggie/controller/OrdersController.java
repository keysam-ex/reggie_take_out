package com.kjy.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kjy.reggie.common.BaseContext;
import com.kjy.reggie.common.R;
import com.kjy.reggie.domain.OrderDetail;
import com.kjy.reggie.domain.Orders;
import com.kjy.reggie.domain.vo.AllPage;
import com.kjy.reggie.dto.OrdersDto;
import com.kjy.reggie.service.OrderDetailService;
import com.kjy.reggie.service.OrdersService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * @author:柯佳元
 * @version:2.7
 */
@Slf4j
@RestController
@RequestMapping("/order")
public class OrdersController {

    @Resource
    private OrdersService ordersService;

    @Resource
    private OrderDetailService orderDetailService;

    /**
     * 用户下单
     *
     * @param orders
     * @return
     */
    @PostMapping("/submit")
    public R<String> submit(@RequestBody Orders orders) {
        log.info("订单的数据={}", orders);
        ordersService.submit(orders);
        return R.success("下单成功");
    }

    /**
     * 此方法用来显示用户最近的订单信息
     * @param page
     * @return
     */
    @GetMapping("/userPage")
    public R<Page> orderPage(AllPage page) {
        Long userId = BaseContext.getCurrentId();
        log.info("page={}", page);
        Page<Orders> ordersPage = new Page<>(page.getPage(), page.getPageSize());
        LambdaQueryWrapper<Orders> ordersLambdaQueryWrapper = new LambdaQueryWrapper<>();
        ordersLambdaQueryWrapper.eq(Orders::getUserId, userId).orderByDesc(Orders::getOrderTime);

        ordersService.page(ordersPage, ordersLambdaQueryWrapper);

        Page<OrdersDto> ordersDtoPage = new Page<>();

        BeanUtils.copyProperties(ordersPage, ordersDtoPage, "records");

        List<Orders> records = ordersPage.getRecords();

        List<OrdersDto> ordersDtos = records.stream().map(item -> {
            OrdersDto ordersDto = new OrdersDto();
            BeanUtils.copyProperties(item, ordersDto);
            LambdaQueryWrapper<OrderDetail> orderDetailLambdaQueryWrapper = new LambdaQueryWrapper<>();
            Long id = item.getId(); //得到订单的id
            orderDetailLambdaQueryWrapper.eq(OrderDetail::getOrderId, id);
            List<OrderDetail> orderDetails = orderDetailService.list(orderDetailLambdaQueryWrapper);
            ordersDto.setOrderDetails(orderDetails);
            return ordersDto;

        }).collect(Collectors.toList());



        ordersDtoPage.setRecords(ordersDtos);


        return R.success(ordersDtoPage);

    }


    /**
     * 再来一单
     * @return
     */
    @PostMapping("/again")
    public R<String > again(){
        return R.success("再来一单");
    }


}
