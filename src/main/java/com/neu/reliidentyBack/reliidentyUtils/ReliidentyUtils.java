package com.neu.reliidentyBack.reliidentyUtils;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.DigestUtils;

import java.util.Map;
import java.util.UUID;

/**
 * @author jasonR
 * @date 2021/4/30 10:28
 * 用于字符串处理
 *
 *
 */
public class ReliidentyUtils {

    //字符串md5加密
    public static String md5(String key){
        if(StringUtils.isBlank(key)) return null;
        //判空则返回null
        return DigestUtils.md5DigestAsHex(key.getBytes());
    }

    //生成随机字符串
    public static String generateUUID(){
        return UUID.randomUUID().toString().replaceAll(" ","");
    }

    //文件md5加密
    public static String md5(byte[] file){
        if(file==null || file.length<=0) return null;
        return DigestUtils.md5DigestAsHex(file);
    }

    //ajax异步请求传输JSON字符串
    public static String getJSONString(int code, String msg, Map<String,Object> map){
        JSONObject json=new JSONObject();
        json.put("code",code);
        json.put("msg",msg);
        if(map!=null){
            map.forEach(json::put);
        }
        return json.toJSONString();
    }
    public static String getJSONString(int code, String msg){
        return getJSONString(code,msg,null);
    }

    public static String getJSONString(int code){
        return getJSONString(code,null,null);
    }

    //用于生成规定python端和java端通信内容
    //格式：imagePath:xxx
    public static String getTCPContent(String imagePath){
        return "imagePath:"+imagePath;
    }

}
