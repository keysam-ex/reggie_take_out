package com.kjy.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kjy.reggie.domain.Employee;
import com.kjy.reggie.service.EmployeeService;
import com.kjy.reggie.mapper.EmployeeMapper;
import org.springframework.stereotype.Service;

/**
* @author 柯佳元
* @description 针对表【employee(员工信息)】的数据库操作Service实现
* @createDate 2022-08-26 01:08:18
*/
@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee>
    implements EmployeeService{

}




