package com.neu.reliidentyBack.reliidentyUtils.enums;

/**
 * @author jasonR
 * @date 2021/3/13 10:28
 *
 * 用于记录登陆状态的枚举类
 */
public enum LoginEnum {
    //默认状态的登陆凭证超时时间
    DEFAULT_EXPIRED_SECONDS(3600 *12),
    REMEMBER_EXPIRED_SECONDS(3600*24*7)
    ;

    private final int times;

    public int getTimes() {
        return times;
    }

    LoginEnum(int times) {
        this.times = times;
    }
}
