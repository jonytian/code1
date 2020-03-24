package com.example.sys.mapper;

import com.example.sys.entity.Video;
import com.example.sys.entity.VideoSearch;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface VideoMapper {

    int delete(Integer id);

    int insert(Video video);

    List<Video> getVideos(@Param("videoSearch") VideoSearch videoSearch);

}
