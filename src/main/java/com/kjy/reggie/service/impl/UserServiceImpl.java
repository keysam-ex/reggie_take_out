package com.kjy.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kjy.reggie.domain.User;
import com.kjy.reggie.service.UserService;
import com.kjy.reggie.mapper.UserMapper;
import org.springframework.stereotype.Service;

/**
* @author 柯佳元
* @description 针对表【user(用户信息)】的数据库操作Service实现
* @createDate 2022-08-29 15:33:21
*/
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService{

}




