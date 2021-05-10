package com.neu.reliidentyBack.controller;

import com.mysql.cj.protocol.PacketReceivedTimeHolder;
import com.neu.reliidentyBack.Annotation.LoginRequired;
import com.neu.reliidentyBack.domain.Ticket;
import com.neu.reliidentyBack.domain.User;
import com.neu.reliidentyBack.reliidentyUtils.CookieUtil;
import com.neu.reliidentyBack.reliidentyUtils.HostHolder;
import com.neu.reliidentyBack.reliidentyUtils.ReliidentyUtils;
import com.neu.reliidentyBack.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * @author jasonR
 * @date 2021/4/30 16:12
 */
@Controller
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private HostHolder hostHolder;

    @RequestMapping(path = "/recharge",method = RequestMethod.GET)
    @ResponseBody
    @LoginRequired
    public String recharge(int rechargeCount){
        User user=hostHolder.getUser();
        Ticket useTicket =userService.findTicketByContent(user.getUseTicket());
        if(useTicket==null){
            return ReliidentyUtils.getJSONString(400,"用户信息不存在");
        }
        userService.updateUseTime(useTicket.getUseTime()+rechargeCount,useTicket.getId());
        return ReliidentyUtils.getJSONString(200,"充值成功");
    }
    //返回用户信息
    @RequestMapping(path = "/UserMessage",method = RequestMethod.GET)
    @ResponseBody
    @LoginRequired
    public String getUserMessage(){
        User user=hostHolder.getUser();
        Map<String,Object> userMessage=new HashMap<>();
        userMessage.put("userName",user.getUsername());
        // 返回使用凭证
        userMessage.put("useTicket",user.getUseTicket());
        // 返回剩余使用次数
        Ticket useTicket=userService.findTicketByContent(user.getUseTicket());
        userMessage.put("useTimes",useTicket.getUseTime());
        return ReliidentyUtils.getJSONString(200,"请求成功",userMessage);
    }




}
