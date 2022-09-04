package com.kjy.reggie.mapper;
import java.util.List;
import org.apache.ibatis.annotations.Param;

import com.kjy.reggie.domain.AddressBook;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
* @author 柯佳元
* @description 针对表【address_book(地址管理)】的数据库操作Mapper
* @createDate 2022-08-29 17:18:49
* @Entity com.kjy.reggie.domain.AddressBook
*/
public interface AddressBookMapper extends BaseMapper<AddressBook> {


    List<AddressBook> selectUserIdById(@Param("id") Long id);
}




