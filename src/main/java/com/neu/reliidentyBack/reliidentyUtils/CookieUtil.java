package com.neu.reliidentyBack.reliidentyUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

/**
 * @author jasonR
 * @date 2021/3/13 11:42
 * 遍历cookie内容得到指定参数值
 */
public class CookieUtil {

    public  static String getValue(HttpServletRequest request,String name){
        if(request==null || name==null){
            throw new IllegalArgumentException("参数为空！");
        }
        Cookie[] cookies= request.getCookies();
        if(cookies!=null){
            for (Cookie cookie : cookies) {
                if(cookie.getName().equals(name)){
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
}
