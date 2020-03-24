package com.example.sys.service;


import com.example.sys.entity.Video;
import com.example.sys.entity.VideoSearch;
import com.example.sys.util.PageDataResult;

/**
 * Created by tyj on 2018/08/15.
 */
public interface VideoService {

	/**
	 * 分页查询用户列表
	 * @param page
	 * @param limit
	 * @return
	 */
	PageDataResult getVideos(VideoSearch videoSearch, int page, int limit);

    int insert(Video video);

    int delete(Integer videoId);
}
