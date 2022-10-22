package com.chen.reggie.filter;

import com.alibaba.fastjson.JSON;
import com.chen.reggie.commmon6.BaseContext;
import com.chen.reggie.commmon6.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 登录过滤器:检查用户是否已完成登录
 * @author chen
 * @create 2022/9/29 21:49
 */

@WebFilter(filterName = "LoginCheckFilter",urlPatterns = "/*")
@Slf4j
public class LoginCheckFilter implements Filter{

    //路径匹配器，支持通配符
    public static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        /**
         * 登录过滤器处理逻辑
         * 1. 获取本次请求的url
         * 2. 判断本次请求是否需要处理
         * 3. 如果不需要处理，则直接放行
         * 4.判断登录状态，如果已登录，则直接放行
         * 5.如果未登录则返回未登录结果
         */

//        1. 获取本次请求的url
        String requestURI = request.getRequestURI();

        log.info("拦截到请求: {}",requestURI);

         //定义不需要处理路径白名单 注：/backend/index.html 字符串写法不一致
        String[] urls = new String[]{
                "/employee/login",
                "/employee/logout",
                "/backend/**",
                "/front/**",
                "/common/**",

                "/user/sendMsg", //移动端发送短信
                "/user/login" //移动端登录

        };

//        2. 判断本次请求是否需要处理
        boolean check = check(urls, requestURI);

//        3. 如果不需要处理，则直接放行
        if (check){
            log.info("本次请求不需要处理",requestURI);
            filterChain.doFilter(request,response);
            return;
        }

//        4-1判断用户登录状态，如果已登录，则直接放行
        if (request.getSession().getAttribute("employee")!=null){
            log.info("用户已登录，用户id为：{}",request.getSession().getAttribute("employee"));

            //设置当前登录用户id
            Long empId = (Long) request.getSession().getAttribute("employee");
            BaseContext.setCurrentId(empId);

            filterChain.doFilter(request,response);
            return;
        }

//        4-2判断移动端用户登录状态，如果已登录，则直接放行
        if (request.getSession().getAttribute("user")!=null){
            log.info("用户已登录，用户id为：{}",request.getSession().getAttribute("user"));

            //设置当前登录用户id
            Long userId = (Long) request.getSession().getAttribute("user");
            BaseContext.setCurrentId(userId);

            filterChain.doFilter(request,response);
            return;
        }


//        5.如果未登录则返回未登录结果,通过输出流方式向客户端页面响应数据
        log.info("用户未登录");
        response.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));
        return;

//        log.info("拦截到请求：{}",request.getRequestURI());
//        //放行
//        filterChain.doFilter(request,response);
    }

    /**封装方法
     * 路径匹配，检查本次请求是否需要放行
     * @param urls
     * @param requestURI
     * @return
     */
    public boolean check(String[] urls,String requestURI){
        for (String url : urls) {
            boolean match = PATH_MATCHER.match(url, requestURI);
            if (match){
                return true;
            }
        }
        return false;

    }

}
