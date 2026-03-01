package com.itheima.interceptor;

import com.alibaba.fastjson.JSON;
import com.itheima.pojo.Result;
import com.itheima.utils.JwtUtils;
import io.jsonwebtoken.Claims;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class LoginCheckInterceptor implements HandlerInterceptor {

    /**
     * 判断用户是否已经登陆，如果登陆：放行，如果未登陆：跳转到登录页
     *
     * @param request  current HTTP request  请求对象
     * @param response current HTTP response  响应对象
     * @param handler  chosen handler to execute, for type and/or instance evaluation
     * @return 如果是true：继续执行下一个拦截器或者控制器，如果是false: 拦截器拦截，不再执行下一个拦截器或者控制器
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 获取请求路径
        String requestURI = request.getRequestURI();
        System.out.println("拦截器执行 - 请求路径: " + requestURI);
        
        // 白名单路径，直接放行（用于注册等不需要登录的功能）
        String[] whiteList = {"/login", "/register", "/depts", "/upload"};
        for (String path : whiteList) {
            if (requestURI.equals(path)) {
                System.out.println("白名单路径，直接放行: " + requestURI);
                return true;
            }
        }
        
        //1. 获取请求头中的token
        String token = request.getHeader("token");
        System.out.println("Token: " + (token != null ? "存在" : "不存在"));
        
        //判断token，如果为空 ，拦截，跳转到登陆页面
        if (token == null || token.isEmpty()) {

            //构建返回结果：未登录
            Result notLogin = Result.error("NOT_LOGIN");
            String resultJson = JSON.toJSONString(notLogin);

            //响应给浏览器
            //设置响应头
            response.setContentType("application/json;charset=utf-8");

            response.getWriter().write(resultJson); //写到响应体

            return false;
        }
        //2. 校验token，如果不通过，跳转到登陆页面

        try {
            Claims claims = JwtUtils.parseJWT(token);
            System.out.println("Token 验证通过");
        } catch (Exception e) {
            //token校验不通过
            System.out.println("Token 验证失败: " + e.getMessage());
            //构建返回结果：未登录
            Result notLogin = Result.error("NOT_LOGIN");
            String resultJson = JSON.toJSONString(notLogin);

            //响应给浏览器
            //设置响应头
            response.setContentType("application/json;charset=utf-8");

            response.getWriter().write(resultJson); //写到响应体

            return false;
        }

        //3.放行
        return true;
    }
}