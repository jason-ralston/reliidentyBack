package com.neu.reliidentyBack.controller;

import com.neu.reliidentyBack.domain.User;
import com.neu.reliidentyBack.reliidentyUtils.CookieUtil;
import com.neu.reliidentyBack.reliidentyUtils.ReliidentyUtils;
import com.neu.reliidentyBack.reliidentyUtils.enums.LoginEnum;
import com.neu.reliidentyBack.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @author jasonR
 * @date 2021/4/30 16:18
 */
@Controller
public class LoginController {
    @Autowired
    private UserService userService;

    @Value("${server.servlet.context-path}")
    private String PATH;

    /**
     * 注册方法
     *
     * */
    @RequestMapping(path = "/register",method = RequestMethod.GET)
    @ResponseBody
    public String register(User user){
        Map<String,Object> map=userService.register(user);
        if(map==null || map.isEmpty()){
            //已经注册成功
            return ReliidentyUtils.getJSONString(200,"注册成功");
        }else{
            return ReliidentyUtils.getJSONString(400,"创建出现问题",map);
        }
    }

    @RequestMapping(path = "/login",method = RequestMethod.GET)
    @ResponseBody
    public String login(String username, String password, boolean rememberme, HttpServletResponse response){

        //验证登陆信息
        int expiredSeconds =rememberme? LoginEnum.REMEMBER_EXPIRED_SECONDS.getTimes(): LoginEnum.DEFAULT_EXPIRED_SECONDS.getTimes();
        Map<String,Object> map=userService.login(username,password,expiredSeconds);
        if (map.containsKey("loginTicket")){
            //登陆成功,并传递登陆凭证cookie
            Cookie cookie =new Cookie("loginTicket",map.get("loginTicket").toString());
            cookie.setPath(PATH);
            response.addCookie(cookie);
            return ReliidentyUtils.getJSONString(200,"登陆成功",map);
        }else{
            return ReliidentyUtils.getJSONString(400,"用户名或密码出现问题",map);
        }
    }

    @RequestMapping(path="/logout",method = RequestMethod.GET)
    @ResponseBody
    public String logout(HttpServletRequest request){
        String ticket = CookieUtil.getValue(request,"ticket");
        userService.UserLogout(ticket);
        return ReliidentyUtils.getJSONString(200,"登出成功");

    }
}
