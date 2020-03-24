package com.example.logsys.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Configuration
public class AdminSecurityConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/static/");
    }

//    @Override
//    public void addInterceptors(InterceptorRegistry registry) {
//        HandlerInterceptor handlerInterceptor = new HandlerInterceptor() {
//            @Override
//            public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
//                User users = (User) request.getSession().getAttribute("users");
//                if (users == null) {
//                    String path = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath();
//                    response.sendRedirect(path+"/users/login");
//                    return false;
//                }else{
//                    return true;
//                }
//            }
//        };
//        //指定进入拦截器
//        registry.addInterceptor(handlerInterceptor).addPathPatterns("/users/**").excludePathPatterns("/users/login1");
//    }



}
