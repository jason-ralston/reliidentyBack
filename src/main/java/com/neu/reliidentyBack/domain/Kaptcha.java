package com.neu.reliidentyBack.domain;

import lombok.Data;

import java.util.Date;

/**
 * @author jasonR
 * @date 2021/5/9 23:43
 * 
 */
@Data
public class Kaptcha {
    private String kaptchaOwner;
    private String kaptchaCode;
    private Date expiredTime;//过期时间


}
