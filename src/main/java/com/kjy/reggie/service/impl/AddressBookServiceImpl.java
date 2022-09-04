package com.kjy.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kjy.reggie.domain.AddressBook;
import com.kjy.reggie.service.AddressBookService;
import com.kjy.reggie.mapper.AddressBookMapper;
import org.springframework.stereotype.Service;

/**
* @author 柯佳元
* @description 针对表【address_book(地址管理)】的数据库操作Service实现
* @createDate 2022-08-29 17:18:50
*/
@Service
public class AddressBookServiceImpl extends ServiceImpl<AddressBookMapper, AddressBook>
    implements AddressBookService{

}




