package com.neu.reliidentyBack.dao;

import com.neu.reliidentyBack.domain.Kaptcha;
import com.neu.reliidentyBack.domain.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;


/**
 * @author jasonR
 * @date 2021/4/30 11:29
 */
@Mapper
@Repository
public interface UserMapper {
    User selectUserById(int id);
    User selectUserByName(String username);
    int insertUser(User user);

    int insertKaptcha(Kaptcha kaptcha);//插入验证码
    Kaptcha selectKaptchaByOwner(String KaptchaOwner);
}
