package com.neu.reliidentyBack.service;

import com.neu.reliidentyBack.dao.ImageMapper;
import com.neu.reliidentyBack.domain.Image;
import com.neu.reliidentyBack.reliidentyUtils.ReliidentyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.List;

/**
 * @author jasonR
 * @date 2021/5/2 15:41
 */
@Service
public class ImageService {
    @Autowired
    private ImageMapper imageMapper;

    @Value("${reliidenty.tcp.port}")
    private int PORT;

    public Image findImageByMD5(String imageMD5) {
        return imageMapper.selectImageByMD5(imageMD5);
    }

    //访问python端,获取分析结果
    public Image analysisImage(Image image,String imagePath) {

        try (Socket client = new Socket("localhost", PORT)) {
            OutputStream os = client.getOutputStream();
            os.write(imagePath.getBytes());
            InputStream is = client.getInputStream();
            byte[] b = new byte[100];
            int len = is.read(b);
            String[] res = new String(b, 0, len).split(",");
            image.setVioProbability(Float.parseFloat(res[0]));
            image.setSexProbability(Float.parseFloat(res[1]));
            image.setAdvProbability(Float.parseFloat(res[2]));
            image.setPolProbability(Float.parseFloat(res[3]));
            image.setSafeProbability(Float.parseFloat(res[4]));
            os.close();
            is.close();
        } catch (IOException e) {
            throw new RuntimeException("创建Socket客户端失败",e);
        }
        return image;


    }


    public int  addImage(Image image) {
        return imageMapper.insertImage(image);

    }

    public List<Image> findImagesByUser(int userId){
        return imageMapper.selectImageByUser(userId);
    }
    public List<Image> findImagesByOwner(String ownerId){
        return imageMapper.selectImageByOwner(ownerId);
    }
}
