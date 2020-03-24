package com.example.sys.mapper;

import com.example.sys.entity.HmVideo;
import com.example.sys.entity.HmVideoSearch;
import com.example.sys.entity.Video;
import com.example.sys.entity.VideoSearch;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface HmVideoMapper {

    int delete(Integer id);

    int insert(HmVideo hmVideo);

    List<Video> findAll(@Param("hmVideoSearch") HmVideoSearch hmVideoSearch);

}
