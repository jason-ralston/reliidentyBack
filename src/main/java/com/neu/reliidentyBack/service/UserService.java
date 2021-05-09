package com.neu.reliidentyBack.service;


import com.neu.reliidentyBack.dao.TicketMapper;
import com.neu.reliidentyBack.dao.UserMapper;
import com.neu.reliidentyBack.domain.Kaptcha;
import com.neu.reliidentyBack.domain.Ticket;
import com.neu.reliidentyBack.domain.User;
import com.neu.reliidentyBack.reliidentyUtils.ReliidentyUtils;
import com.neu.reliidentyBack.reliidentyUtils.enums.TicketTypeEnum;
import com.neu.reliidentyBack.reliidentyUtils.enums.UserTypeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author jasonR
 * @date 2021/4/30 14:36
 */
@Service
public class UserService {
    @Autowired
    UserMapper userMapper;

    @Autowired
    TicketMapper ticketMapper;

    @Value("${reliidenty.defaultTestUserCount}")
    private int DEFAULT_TEST_USER_COUNT;

    public User findUserById(int id){
        return userMapper.selectUserById(id);
    }

    public User findUserByName(String name){
        return userMapper.selectUserByName(name);
    }

    @Transactional(isolation = Isolation.READ_COMMITTED,propagation = Propagation.REQUIRED)
    public Map<String, Object> register(User user) {
        Map<String,Object> map=new HashMap<>();
        //先对空值做校验
        if(user==null){
            throw new IllegalArgumentException("参数不能为空");
        }
        User u=userMapper.selectUserByName(user.getUsername());
        //验证账号
        if(u!=null){
            map.put("usernameMsg","账号id已经存在");
            return map;
        }
        //处理注册逻辑
        //加盐
        user.setSalt(ReliidentyUtils.generateUUID().substring(0,5));
        //加盐之后对密码加密
        user.setPassword(ReliidentyUtils.md5(user.getPassword()+user.getSalt()));
        user.setCreateTime(new Date());
        user.setType(UserTypeEnum.TEST_USER);
        //生成随机使用凭证内容
        String ticketContent =ReliidentyUtils.generateUUID();
        user.setUseTicket(ticketContent);
        int userId = userMapper.insertUser(user);
        //生成使用凭证
        Ticket useTick=new Ticket();
        useTick.setTicketType(TicketTypeEnum.USE_TICKET.getValue());
        useTick.setUserId(userId);
        useTick.setUseTime(DEFAULT_TEST_USER_COUNT);
        useTick.setContent(ticketContent);
        ticketMapper.insertUseTicket(useTick);
        return map;
    }

    public Map<String, Object> login(String username,String password,int expireSeconds) {
        Map<String,Object> map =new HashMap<>();

        //验证账号是否为空
        User user=userMapper.selectUserByName(username);
        if(user==null){
            map.put("usernameMsg","账户不存在");
            return map;
        }

        //验证密码
        password=ReliidentyUtils.md5(password+user.getSalt());
        if(!user.getPassword().equals(password)){
            map.put("passwordMsg","密码错误");
            return map;
        }
        //登陆验证成功

        //生成登陆凭证
        Ticket loginTicket=new Ticket();
        //设置凭证信息
        loginTicket.setUserId(user.getId());
        loginTicket.setContent(ReliidentyUtils.generateUUID());
        loginTicket.setTicketType(TicketTypeEnum.LOGIN_TICKET.getValue());
        loginTicket.setExpired(new Date(System.currentTimeMillis()+expireSeconds*1000));
        ticketMapper.insertLoginTicket(loginTicket);
        map.put("loginTicket",loginTicket.getContent());
        return map;
    }
    //通过凭证内容找到凭证
    public Ticket findTicketByContent(String content){
        return ticketMapper.selectTicketByContent(content);
    }

    //更新凭证信息
    public int updateUseTime(int time,int id){
        return ticketMapper.updateUseTime(time,id);
    }

    //用户登出
    public void UserLogout(String ticket){
        ticketMapper.updateStatus(ticket);

    }
    //查找验证码
    public Kaptcha findKaptcha(String kaptchaOwner){
        return userMapper.selectKaptchaByOwner(kaptchaOwner);
    }

    //插入验证码
    public void addKaptcha(Kaptcha kaptcha){
        userMapper.insertKaptcha(kaptcha);
    }

}
