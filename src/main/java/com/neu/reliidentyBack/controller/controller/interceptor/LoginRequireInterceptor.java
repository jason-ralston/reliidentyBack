package com.neu.reliidentyBack.controller.controller.interceptor;

import com.neu.reliidentyBack.Annotation.LoginRequired;
import com.neu.reliidentyBack.reliidentyUtils.HostHolder;
import com.neu.reliidentyBack.reliidentyUtils.ReliidentyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.lang.reflect.Method;

/**
 * @author jasonR
 * @date 2021/5/2 21:25
 */
@Component
public class LoginRequireInterceptor implements HandlerInterceptor {
    @Autowired
    HostHolder hostHolder;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if(handler instanceof HandlerMethod){
            //判断handler请求是不是一个方法
            HandlerMethod handlerMethod=(HandlerMethod) handler;
            //如果是，那么则拿到这个方法
            Method method = handlerMethod.getMethod();
            //获取注解进行判断
            //如果这个请求方法打上了这个自定义注解，那么就需要检查用户的登陆状态
            LoginRequired loginRequired=method.getAnnotation(LoginRequired.class);
            if (loginRequired !=null && hostHolder.getUser()==null){
                PrintWriter writer= response.getWriter();
                writer.write(ReliidentyUtils.getJSONString(404,"资源不存在"));
                return false;
            }


        }

        return true;
    }
}
