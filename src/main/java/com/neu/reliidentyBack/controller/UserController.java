package com.neu.reliidentyBack.controller;

import com.neu.reliidentyBack.Annotation.LoginRequired;
import com.neu.reliidentyBack.domain.Ticket;
import com.neu.reliidentyBack.domain.User;
import com.neu.reliidentyBack.reliidentyUtils.HostHolder;
import com.neu.reliidentyBack.reliidentyUtils.ReliidentyUtils;
import com.neu.reliidentyBack.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

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







}
