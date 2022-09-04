package com.kjy.reggie;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.annotation.Resource;

/**
 * @author:柯佳元
 * @version:2.7
 */
@SpringBootTest
public class AppTests {

    @Resource
    private JdbcTemplate jdbcTemplate;

    @Test
    public void t1(){
        System.out.println(jdbcTemplate.getDataSource().getClass());
    }
}
