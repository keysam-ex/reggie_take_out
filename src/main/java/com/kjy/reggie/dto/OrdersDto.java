package com.kjy.reggie.dto;


import com.kjy.reggie.domain.OrderDetail;
import com.kjy.reggie.domain.Orders;
import lombok.Data;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Data
public class OrdersDto extends Orders {

    private String userName;

    private String phone;

    private String address;

    private String consignee;

    private List<OrderDetail> orderDetails;



	
}
