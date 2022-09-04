package com.kjy.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.kjy.reggie.common.R;
import com.kjy.reggie.domain.User;
import com.kjy.reggie.service.UserService;
import com.kjy.reggie.utils.SMSUtils;
import com.kjy.reggie.utils.ValidateCodeUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * @author:柯佳元
 * @version:2.7
 */
@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {


    @Resource
    private UserService userService;


    /**
     * 模拟发送验证码
     *
     * @param user
     * @param session
     * @return
     */
    @PostMapping("/sendMsg")
    public R<String> sendMsg(@RequestBody User user, HttpSession session) {
        String phone = user.getPhone();

        if (StringUtils.isNotEmpty(phone)) {
            //生成随机的验证码
            String code = ValidateCodeUtils.generateValidateCode(4).toString();
            log.info("验证码={}", code);

            //调用阿里云提供的短信服务API完成发送短信
            //SMSUtils.sendMessage("瑞吉外卖","",phone,code);

            session.setAttribute(phone, code);
            return R.success("手机验证码发送成功");
        }

        return R.error("手机验证码发送失败");

    }

    @PostMapping("/login")
    public R<Object> login(HttpSession session, @RequestBody Map<String, Object> map) {
        // (String) session.getAttribute(phone)
        //获取提交过来的手机号和验证码
        String phone = map.get("phone").toString();
        String code = map.get("code").toString();

        //校验
        if (StringUtils.isNotEmpty(phone) && StringUtils.isNotEmpty(code)) {

            //进行证码比对,[页面提交的验证码和Session中的验证码比对]
            String attribute = (String) session.getAttribute(phone);
            if (attribute != null && attribute.equals(code)) {

                LambdaQueryWrapper<User> userLambdaQueryWrapper = new LambdaQueryWrapper<>();
                userLambdaQueryWrapper.eq(User::getPhone, phone);
                User user = userService.getOne(userLambdaQueryWrapper);
                //判断当前手机号对应的用户是否为新用户,如果是新用户就自动完成注册
                if (user == null) {

                    user = new User();
                    user.setPhone(phone);
                    user.setStatus(1);
                    userService.save(user);


                }
                //session.removeAttribute(phone); //清除session对应的验证码
                session.setAttribute("user", user.getId());
                return R.success(user);


            }

        }
        return R.success("登录有误,请重新登录!");


    }

    /**
     * 此方法用于处理用户登出
     * @param session
     * @return
     */
    @PostMapping("/loginout")
    public R<String > logout(HttpSession session){
        session.removeAttribute("user");
        return R.success("退出成功");
    }
}
