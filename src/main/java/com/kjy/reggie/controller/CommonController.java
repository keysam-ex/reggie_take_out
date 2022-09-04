package com.kjy.reggie.controller;

import com.kjy.reggie.common.R;
import com.kjy.reggie.utils.WebUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;


import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.UUID;


/**
 * @author:柯佳元
 * @version:2.7 通用的 用于 处理 文件上传下载
 */
@RestController
@Slf4j
@RequestMapping("/common")
public class CommonController {


    @Value("${reggie.fileuploadpath}")
    String uploadPath;

    // 专门来处理文件上传
    @PostMapping("/upload")
    public R<String> upload(MultipartFile file) throws Exception {
        // file 是一个临时文件,需要转存到指定位置,否则本次请求后临时文件会删除
        log.info(file.toString());

        //1、为文件生成一个名字
        String fileName = UUID.randomUUID().toString() + WebUtils.getSuffix(file.getOriginalFilename());

        //2.构建上传的目录,咱们自己用目录的形式存放文件
        File f = new File(uploadPath);
        if (!f.exists()) {
            f.mkdirs();
        }
        String absolutePath = f.getAbsolutePath();
        log.info(absolutePath);
        file.transferTo(new File(absolutePath + "/" + fileName));

        return R.success(fileName);
    }

    // 专门来处理文件下载
    @GetMapping("/download")
    public void download(String name, HttpServletResponse response) throws Exception {
        //log.info("哈哈哈={}",name);
        BufferedInputStream bis = new BufferedInputStream(new FileInputStream(new File(uploadPath + name)));
        response.setContentType("image/*");
        ServletOutputStream outputStream = response.getOutputStream();
        IOUtils.copy(bis, outputStream);
        bis.close();
        outputStream.close();


    }

}
