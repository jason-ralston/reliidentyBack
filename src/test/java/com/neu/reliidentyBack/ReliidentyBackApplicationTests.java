package com.neu.reliidentyBack;

import com.neu.reliidentyBack.controller.ImageController;
import com.neu.reliidentyBack.domain.Image;
import com.neu.reliidentyBack.reliidentyUtils.ReliidentyUtils;
import com.neu.reliidentyBack.service.ImageService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.*;

@SpringBootTest
class ReliidentyBackApplicationTests {
	@Autowired
	ImageService imageService;

	@Autowired
	ImageController controller;

	@Test
	void contextLoads() {
	}

	@Test
	void testAnalysisImage(){
		imageService.analysisImage(new Image(),"123");
	}

	@Test
	void testMap(){
		List<Image> imageList=new ArrayList<>();
		imageList.add(new Image("1",1,"1",0.1f,0.1f,0.1f,0.1f,new Date()));
		imageList.add(new Image("1",1,"1",0.1f,0.1f,0.1f,0.1f,new Date()));
		imageList.add(new Image("1",1,"1",0.1f,0.1f,0.1f,0.1f,new Date()));
		Map<String,Object> map=new HashMap<>();
		map.put("res",imageList);
		System.out.println(ReliidentyUtils.getJSONString(200,"ok",map));

	}
	@Test
	void testSelect(){
		List<Image> imageList = imageService.findImagesByUser(1);
		if(imageList==null || imageList.isEmpty()){
			System.out.println(ReliidentyUtils.getJSONString(204,"尚未进行过识别"));
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
		System.out.println(ReliidentyUtils.getJSONString(200,"查询成功",map));
	}

}
