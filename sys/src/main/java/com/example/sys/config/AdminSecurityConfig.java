package com.example.sys.config;

import com.example.sys.entity.User;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
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
////    public void addInterceptors(InterceptorRegistry registry) {
////        HandlerInterceptor handlerInterceptor = new HandlerInterceptor() {
////            @Override
////            public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
////                User user = (User) request.getSession().getAttribute("user");
////                if (user == null) {
////                    String path = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath();
////                    response.sendRedirect(path+"/admin/login");
////                    return false;
////                }else{
////                    return true;
////                }
////            }
////        };
////        //指定进入拦截器
////        registry.addInterceptor(handlerInterceptor).addPathPatterns("/admin/**").excludePathPatterns("/admin/login1");
////    }





}
