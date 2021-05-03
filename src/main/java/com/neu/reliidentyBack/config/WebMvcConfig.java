package com.neu.reliidentyBack.config;

import com.neu.reliidentyBack.controller.interceptor.LoginRequireInterceptor;
import com.neu.reliidentyBack.controller.interceptor.LoginTicketInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author jasonR
 * @date 2021/5/3 15:21
 * 拦截器配置
 *
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    @Autowired
    private LoginRequireInterceptor loginRequireInterceptor;

    @Autowired
    private LoginTicketInterceptor loginTicketInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginTicketInterceptor)
                .excludePathPatterns("/*/*.css","/*/*.js","/*/*.png","/*/*.jpg","/*/*.jpeg");
        //这个方法表示不做拦截的路径
        //addPathPatterns();
        //表示拦截哪些域名下面
        registry.addInterceptor(loginRequireInterceptor)
                .excludePathPatterns("/*/*.css","/*/*.js","/*/*.png","/*/*.jpg","/*/*.jpeg");
    }


}
