package com.neu.reliidentyBack.controller;

import com.google.code.kaptcha.Producer;
import com.neu.reliidentyBack.domain.Image;
import com.neu.reliidentyBack.domain.Kaptcha;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.imageio.ImageIO;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.Timestamp;
import java.util.Date;
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

    @Value("${reliidenty.codePassTime}")
    private int codePassTime;
    /**
     * 注册方法
     *
     * */
    @RequestMapping(path = "/register",method = RequestMethod.POST)
    @ResponseBody
    public String register(User user){
        Map<String,Object> map=userService.register(user);
        if(map==null || map.isEmpty()){
            //已经注册成功
            return ReliidentyUtils.getJSONString(200,"注册成功");
        }else{
            return ReliidentyUtils.getJSONString(400,"注册出现问题",map);
        }
    }

    @RequestMapping(path = "/login",method = RequestMethod.POST)
    @ResponseBody
    public String login(@RequestParam("username") String username, @RequestParam("password") String password, @RequestParam("rememberme") boolean rememberme, @RequestParam("code") String code, HttpServletRequest request, HttpServletResponse response){
       //去数据库里查验证码
        String kaptchaOwner = CookieUtil.getValue(request,"kaptchaOwner");
        Kaptcha kaptcha=userService.findKaptcha(kaptchaOwner);
        long timestamp = new Date().getTime();
        if(kaptcha==null||timestamp<kaptcha.getExpiredTime().getTime()||!code.toUpperCase().equals(kaptcha.getKaptchaCode())){
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

    @RequestMapping(path = "/kaptcha",method = RequestMethod.GET)
    public void getKaptcha(HttpServletResponse response){
        String text=kaptchaProducer.createText();
        String kaptchaOwner=ReliidentyUtils.generateUUID();
        //将验证码信息存在数据库中

        Kaptcha kaptcha=new Kaptcha();
        kaptcha.setKaptchaOwner(kaptchaOwner);
        kaptcha.setKaptchaCode(text);
        kaptcha.setExpiredTime(new Timestamp(new Date(System.currentTimeMillis()+codePassTime*1000).getTime()));
        userService.addKaptcha(kaptcha);
        //给reponse中加入cookie
        response.addCookie(new Cookie("kaptchaOwner",kaptchaOwner));

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

    @RequestMapping(path="/logout",method = RequestMethod.POST)
    @ResponseBody
    public String logout(HttpServletRequest request){
        String ticket = CookieUtil.getValue(request,"ticket");
        userService.UserLogout(ticket);
        return ReliidentyUtils.getJSONString(200,"登出成功");

    }
}
