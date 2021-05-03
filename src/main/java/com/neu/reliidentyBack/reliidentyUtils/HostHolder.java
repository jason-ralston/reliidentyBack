package com.neu.reliidentyBack.reliidentyUtils;

import com.neu.reliidentyBack.domain.User;
import org.springframework.stereotype.Component;

/**
 * @author jasonR
 * @date 2021/5/2 18:20
 *
 * 保存用户的个人信息，本质是threadLocal
 */
@Component
public class HostHolder {
    private ThreadLocal<User> users=new ThreadLocal<>();
    public void setUser(User user){
        users.set(user);
    }

    public User getUser() {
        return users.get();
    }

    public void clear(){
        users.remove();
    }
}
