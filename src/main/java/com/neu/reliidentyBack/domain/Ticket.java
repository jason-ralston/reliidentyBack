package com.neu.reliidentyBack.domain;

import lombok.Data;

import java.util.Date;

/**
 * @author jasonR
 * @date 2021/4/30 18:02
 */
@Data
public class Ticket {
    private int id; //凭证id
    private int userId; //持有者id
    private String content;
    private int ticketType; //凭证类型 1代表登陆凭证，2代表使用凭证
    private int status; //是否有效
    private Date expired; //有效时间
    private int useTime; //使用次数



}
