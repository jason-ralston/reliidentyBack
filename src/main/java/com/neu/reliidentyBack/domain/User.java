package com.neu.reliidentyBack.domain;

import com.neu.reliidentyBack.reliidentyUtils.enums.UserTypeEnum;
import lombok.Data;

import java.util.Date;

/**
 * @author jasonR
 * @date 2021/4/30 10:23
 */
@Data
public class User {
    private int id;
    private String username; //昵称
    private String password;
    private String salt; //盐
    private Date createTime; //创建时间
    private UserTypeEnum Type; //用户服务类型
    private String UseTicket; //用户使用凭证




 }
