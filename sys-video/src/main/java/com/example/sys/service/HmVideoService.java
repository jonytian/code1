package com.example.sys.service;


import com.example.sys.entity.HmVideo;
import com.example.sys.entity.HmVideoSearch;
import com.example.sys.entity.Video;
import com.example.sys.entity.VideoSearch;
import com.example.sys.util.PageDataResult;

/**
 * Created by tyj on 2018/08/15.
 */
public interface HmVideoService {

	PageDataResult findAll(HmVideoSearch hmVideoSearch, int page, int limit);

    int insert(HmVideo hmVideo);

    int delete(Integer videoId);
}
