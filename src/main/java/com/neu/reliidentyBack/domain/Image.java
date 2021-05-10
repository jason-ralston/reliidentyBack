package com.neu.reliidentyBack.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;

/**
 * @author jasonR
 * @date 2021/4/30 10:48
 */
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Image {
    private String imageMD5; //图片生成的md5码
    private int userId; //使用者id
    private String ownerId; //用户提供的，图片所有者id
    //暴力政治广告色情
    private float vioProbability; //暴力类型的违规概率
    private float sexProbability; //色情类型的违规概率
    private float advProbability; //广告类型的违规概率
    private float polProbability; //政治类型违规概率
    private float safeProbability; //安全的概率
    private Date createTime;




}
