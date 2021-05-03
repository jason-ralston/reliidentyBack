package com.neu.reliidentyBack.reliidentyUtils.enums;

/**
 * @author jasonR
 * @date 2021/5/2 14:36
 */
public enum TicketTypeEnum {
    USE_TICKET(2),
    LOGIN_TICKET(1);
    int value;
    public int getValue() {
        return value;
    }
    TicketTypeEnum(int value){
        this.value=value;
    }
}
