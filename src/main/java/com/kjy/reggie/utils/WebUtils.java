package com.kjy.reggie.utils;

import com.baomidou.mybatisplus.core.toolkit.AES;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author:柯佳元
 * @version:2.7
 */
public class WebUtils {

    private static final String fileuploadpath =  "D:\\JAVA\\JAVAEE_projects\\imgs\\";

    public static String getDate(){
        LocalDate now = LocalDate.now();
        return now.getYear() + "\\" + now.getMonthValue() + "\\" + now.getDayOfMonth() + "\\";
    }

    public static void main(String[] args) {
        long orderId = IdWorker.getId();//随机生成订单号
        System.out.println("orderId = " + orderId);

    }

    public static String getSuffix(String fileName){
        return fileName.substring(fileName.lastIndexOf("."));
    }
}

class Sun extends Father{

}
class Father{
    private String name;

    public String getName() {
        return name;
    }
}
