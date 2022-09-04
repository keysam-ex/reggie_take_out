package com.kjy.reggie.domain.vo;

import lombok.Data;

/**
 * @author:柯佳元
 * @version:2.7
 */
@Data
public class AllPage {

    private long page = 1;
    private long pageSize = 10;
    private  String name = "";
}
