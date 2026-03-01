package com.itheima.config;

import com.itheima.interceptor.LoginCheckInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration//代表当前类为配置类
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private LoginCheckInterceptor loginCheckInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginCheckInterceptor)
                .addPathPatterns("/**")  //要拦截的路径
                .excludePathPatterns(
                    "/login",           // 登录接口
                    "/register",        // 注册接口
                    "/depts",           // 部门列表（注册页面需要）
                    "/upload",          // 文件上传
                    "/uploads/**",      // 上传文件访问
                    "/index.html",      // 静态页面
                    "/register.html",   // 注册页面
                    "/*.html",          // 所有HTML
                    "/*.css",           // 所有CSS
                    "/*.js",            // 所有JS
                    "/*.ico",           // 图标
                    "/favicon.ico",     // 网站图标
                    "/error"            // 错误页面
                );//不拦截直接放行的路径
    }
    
    /**
     * 配置静态资源映射
     * 将 /uploads/** 映射到项目根目录的 uploads 文件夹
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String projectPath = System.getProperty("user.dir");
        String uploadPath = "file:" + projectPath + java.io.File.separator + "uploads" + java.io.File.separator;
        
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations(uploadPath);
        
        System.out.println("静态资源映射: /uploads/** -> " + uploadPath);
    }
}