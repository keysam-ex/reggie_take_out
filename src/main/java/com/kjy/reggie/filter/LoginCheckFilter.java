package com.kjy.reggie.filter;

import com.alibaba.fastjson.JSON;
import com.kjy.reggie.common.BaseContext;
import com.kjy.reggie.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author:柯佳元
 * @version:2.7 检查用户是否登录
 */
@Slf4j
@WebFilter(filterName = "LoginCheckFilter", urlPatterns = "/*")
public class LoginCheckFilter implements Filter {

    //路径匹配器可以用来匹配通配符
    public static final AntPathMatcher ANT_PATH_MATCHER = new AntPathMatcher();

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        //1、获取本次请求的URI
        String requestURI = request.getRequestURI();

        //2、定义不需要处理的请求路径
        String[] urls = new String[]{"/employee/login", "/employee/logout", "/backend/**", "/front/**","/user/sendMsg","/user/login"};


        //3、判断本次请求的路径是否不需要拦截 直接放行
        boolean flag = check(urls, requestURI);
        if (flag) {
            log.info("此资源{}不需要拦截", requestURI);
            filterChain.doFilter(request, response);
            return;
        }

        //4.1、判断用户是否登录过,如果登录,直接放行

        if (request.getSession().getAttribute("employee") != null) {
            Long EmployeeId = (Long) request.getSession().getAttribute("employee");
            BaseContext.setCurrentId(EmployeeId);
            log.info("后台用户已登录,ID={}", EmployeeId);
            filterChain.doFilter(request, response);
            return;
        }


        //4.2、判断用户是否登录过,如果登录,直接放行

        if (request.getSession().getAttribute("user") != null) {

            Long userId = (Long) request.getSession().getAttribute("user");
            BaseContext.setCurrentId(userId);
            log.info("前台用户已登录,ID={}", userId);
            filterChain.doFilter(request, response);
            return;
        }



        //5、用户没有登录,需要根据前端需要返回对应格式，让其跳转到登录页面

        /** 前端 需要的json格式,我们通过 getWriter输出 将数据响应客户端返回对应的 json 数据
         * // 响应拦截器
         *   service.interceptors.response.use(res => {
         *       if (res.data.code === 0 && res.data.msg === 'NOTLOGIN') {// 返回登录页面
         *         console.log('---/backend/page/login/login.html---')
         *         localStorage.removeItem('userInfo')
         *         window.top.location.href = '/backend/page/login/login.html'
         *       } else {
         *         return res.data
         *       }
         *     },
         */
        log.info("用户未登录,跳转到登录页面");
        response.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));
        return;

    }

    /**
     * 路径匹配,检查本次请求是否要放行
     *
     * @param urls
     * @param requestURI
     * @return
     */
    public boolean check(String[] urls, String requestURI) {
        for (String url : urls) {
            if (ANT_PATH_MATCHER.match(url, requestURI)) { //判断两者是否匹配上
                return true;
            }
        }
        return false;
    }
}
