package com.neu.reliidentyBack.controller.interceptor;

import com.neu.reliidentyBack.domain.Ticket;
import com.neu.reliidentyBack.domain.User;
import com.neu.reliidentyBack.reliidentyUtils.CookieUtil;
import com.neu.reliidentyBack.reliidentyUtils.HostHolder;
import com.neu.reliidentyBack.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 * @author jasonR
 * @date 2021/5/2 21:18
 */
@Component
public class LoginTicketInterceptor implements HandlerInterceptor {
    @Autowired
    private UserService userService;
    @Autowired
    private HostHolder hostHolder;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //前置拦截
        //从请求中取出cookie
        String ticket= CookieUtil.getValue(request,"loginTicket");

        //验证登陆凭证是否有效
        if (ticket!=null){
            Ticket loginTicket =userService.findTicketByContent(ticket);
            //凭证有效
            if(loginTicket!=null && loginTicket.getStatus()==0 && loginTicket.getExpired().after(new Date())){
                User user= userService.findUserById(loginTicket.getUserId());
                hostHolder.setUser(user);
            }
        }
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        hostHolder.clear();
    }
}
