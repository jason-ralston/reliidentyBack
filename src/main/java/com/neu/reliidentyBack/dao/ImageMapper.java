package com.neu.reliidentyBack.dao;

import com.neu.reliidentyBack.domain.Image;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author jasonR
 * @date 2021/5/2 15:37
 */
@Repository
@Mapper
public interface ImageMapper {
    Image selectImageByMD5(String imageMD5);

    int insertImage(Image image);

    List<Image> selectImageByUser(int userId);

    List<Image> selectImageByOwner(String ownerId);
}
