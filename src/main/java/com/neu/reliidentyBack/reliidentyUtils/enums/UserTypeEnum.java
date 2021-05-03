package com.neu.reliidentyBack.reliidentyUtils.enums;

/**
 * @author jasonR
 * @date 2021/4/30 10:32
 * 用户权限枚举类
 */
public enum UserTypeEnum {
    TEST_USER(1),
    CLASSIFY_USER(2),
    UNCLASSIFY_USER(3);
    private final int value;
    public int getValue() {
        return value;
    }
    UserTypeEnum(int value){
        this.value=value;
    }
}
