package com.neu.reliidentyBack.controller;

import com.google.code.kaptcha.Producer;
import com.neu.reliidentyBack.domain.Image;
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

import javax.imageio.ImageIO;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

/**
 * @author jasonR
 * @date 2021/4/30 16:18
 */
@Controller
public class LoginController {
    @Autowired
    private UserService userService;
    @Autowired
    private Producer kaptchaProducer;

    @Value("${reliidenty.cookiePath.loginTicket}")
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
    public String login(String username, String password, boolean rememberme,String code,HttpServletResponse response,HttpSession session){
        String kaptchaCode=(String) session.getAttribute("kaptchaText");
        if(!code.equals(kaptchaCode)){
            return ReliidentyUtils.getJSONString(400,"验证码错误");
        }
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

    @RequestMapping(path = "kaptcha",method = RequestMethod.GET)
    public void getKaptcha(HttpServletResponse response, HttpSession session){
        String text=kaptchaProducer.createText();
        //将验证码信息存在session里
        session.setAttribute("kaptchaText",text);
        BufferedImage image= kaptchaProducer.createImage(text);
        response.setContentType("image.png");
        //传输文件到前端
        try{
            OutputStream os=response.getOutputStream();
            ImageIO.write(image,"png",os);
        }catch (IOException e){
            throw new RuntimeException("验证码上传失败",e);
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
