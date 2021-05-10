package com.neu.reliidentyBack.controller;

import com.neu.reliidentyBack.Annotation.LoginRequired;
import com.neu.reliidentyBack.domain.Image;
import com.neu.reliidentyBack.domain.Ticket;
import com.neu.reliidentyBack.domain.User;
import com.neu.reliidentyBack.reliidentyUtils.HostHolder;
import com.neu.reliidentyBack.reliidentyUtils.ReliidentyUtils;
import com.neu.reliidentyBack.service.ImageService;
import com.neu.reliidentyBack.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

import java.util.*;

/**
 * @author jasonR
 * @date 2021/5/2 15:28
 */
@Controller
@RequestMapping(path = "/image")
public class ImageController {
    @Autowired
    private ImageService imageService;

    @Autowired
    private UserService userService;

    @Value("${reliidenty.path.upload}")
    private String UPLOAD_PATH;

    @Autowired
    private HostHolder hostHolder;


    // http://localhost:8000/reliidenty/image/anaResUnlogin
    @RequestMapping(path = "/anaResUnLogin",method = RequestMethod.POST)
    @ResponseBody
    public String getAnalysisResult(@RequestParam("imageFile") MultipartFile imageFile,String ownerId,@RequestParam("content") String content){
        Map<String,Object> map=new HashMap<>();
        Ticket useTicket=userService.findTicketByContent(content);
        if(useTicket==null){
            return ReliidentyUtils.getJSONString(400,"凭证错误");
        }
        if(useTicket.getUseTime()<=0){
            return ReliidentyUtils.getJSONString(400,"使用次数不足，请充值");
        }
        User user=userService.findUserById(useTicket.getUserId());

        try{
            //验证是否已经进行过检测
            String imageMD5=ReliidentyUtils.md5(imageFile.getBytes());
            Image image= imageService.findImageByMD5(imageMD5);
            if(image!=null){
                map.put("vio",image.getVioProbability());
                map.put("sex",image.getSexProbability());
                map.put("adv",image.getAdvProbability());
                map.put("pol",image.getPolProbability());
                //扣除使用次数
                userService.updateUseTime(useTicket.getUseTime()-1,useTicket.getId());
                return ReliidentyUtils.getJSONString(200,"请求成功",map);
            }
            //先将文件保存在服务器
            String originalFilename =imageFile.getOriginalFilename();
            String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
            //验证文件格式
            if(StringUtils.isBlank(suffix)){
                return ReliidentyUtils.getJSONString(400,"文件格式为空");
            }
            if(!(suffix.equals(".jpg") || suffix.equals(".png") || suffix.equals(".jpeg"))){
                return ReliidentyUtils.getJSONString(400,"格式暂时不受支持");
            }
            //生成随机文件名
            String filename = ReliidentyUtils.generateUUID()+suffix;

            //新文件存储目录
            File dest =new File(UPLOAD_PATH+"/"+filename);
            String path=dest.getPath();
            //存储文件
            try{
                imageFile.transferTo(dest);
            }catch (IOException e){
                throw  new RuntimeException("文件上传失败",e);
            }
            image=new Image();
            //设置图片信息
            image.setImageMD5(imageMD5);
            if(ownerId!=null){
                image.setOwnerId(ownerId);
            }
            image.setUserId(user.getId());
            //python端处理数据
             image = imageService.analysisImage(image,path);
             image.setCreateTime(new Date());
             map.put("vio",image.getVioProbability());
             map.put("sex",image.getSexProbability());
             map.put("adv",image.getAdvProbability());
             map.put("pol",image.getPolProbability());

            //扣除使用次数
            userService.updateUseTime(useTicket.getUseTime()-1,useTicket.getId());
            //存储图片信息
            imageService.addImage(image);

        }catch (IOException e){
            throw new RuntimeException("文件解析失败",e);
        }catch (RuntimeException e){
            throw new RuntimeException(e);
        }
        return ReliidentyUtils.getJSONString(200,"请求成功",map);
    }

    // http://localhost:8000/reliidenty/image/anaResLogin
    @RequestMapping(path = "/anaResLogin",method = RequestMethod.POST)
    @ResponseBody
    @LoginRequired
    public String getAnalysisResult(@RequestParam("imageFile") MultipartFile imageFile,String ownerId){
        return getAnalysisResult(imageFile,ownerId,hostHolder.getUser().getUseTicket());
    }
    // http://localhost:8000/reliidenty/image/anaResLoginNoOwner
    @RequestMapping(path = "/anaResLoginNoOwner",method = RequestMethod.POST)
    @ResponseBody
    @LoginRequired
    public String getAnalysisResult(@RequestParam("imageFile") MultipartFile imageFile){
        User user=hostHolder.getUser();
        return getAnalysisResult(imageFile,null,hostHolder.getUser().getUseTicket());
    }

    @RequestMapping(path = "/userImages",method = RequestMethod.GET)
    @ResponseBody
    @LoginRequired
    public String getUserImages(){
        User user=hostHolder.getUser();
        List<Image> imageList = imageService.findImagesByUser(user.getId());

        if(imageList==null || imageList.isEmpty()){
            return ReliidentyUtils.getJSONString(204,"尚未进行过识别");
        }
        Map<String,Object> map=new HashMap<>();
        Map<String,List<Image>> imageVo=new HashMap<>();
        for (Image image : imageList) {
            List<Image> list;
            if(!imageVo.containsKey(image.getOwnerId())){
                list=new ArrayList<>();
            }else{
                list=imageVo.get(image.getOwnerId());
            }
            list.add(image);
            imageVo.put(image.getOwnerId(),list);
        }
        map.put("res",imageVo);
        return ReliidentyUtils.getJSONString(200,"查询成功",map);
    }

    @RequestMapping(path = "/ownerImages",method = RequestMethod.GET)
    @ResponseBody
    @LoginRequired
    public String getOwnerImages(String ownerId){
        List<Image> imageList = imageService.findImagesByOwner(ownerId);
        if(imageList==null || imageList.isEmpty()){
            return ReliidentyUtils.getJSONString(204,"系统内不存在该用户的识别记录");
        }
        Map<String,Object> map=new HashMap<>();
        map.put("res",imageList);
        return ReliidentyUtils.getJSONString(200,"查询成功",map);
    }




}
