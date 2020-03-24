package com.example.sys.service;

import com.example.sys.entity.Video;
import com.example.sys.entity.VideoSearch;
import com.example.sys.mapper.VideoMapper;
import com.example.sys.util.DateUtils;
import com.example.sys.util.PageDataResult;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * Created by tyj on 2018/08/15.
 */
@Service
@Slf4j
public class VideoServiceImpl implements VideoService {

	@Autowired
	private VideoMapper videoMapper;


	@Override
	public PageDataResult getVideos(VideoSearch videoSearch, int page, int limit) {
		// 时间处理
		if (null != videoSearch) {
			if (StringUtils.isNotEmpty(videoSearch.getInsertTimeStart())
					&& StringUtils.isEmpty(videoSearch.getInsertTimeEnd())) {
				videoSearch.setInsertTimeEnd(DateUtils.format(new Date()));
			} else if (StringUtils.isEmpty(videoSearch.getInsertTimeStart())
					&& StringUtils.isNotEmpty(videoSearch.getInsertTimeEnd())) {
				videoSearch.setInsertTimeStart(DateUtils.format(new Date()));
			}
			if (StringUtils.isNotEmpty(videoSearch.getInsertTimeStart())
					&& StringUtils.isNotEmpty(videoSearch.getInsertTimeEnd())) {
				if (videoSearch.getInsertTimeEnd().compareTo(
						videoSearch.getInsertTimeStart()) < 0) {
					String temp = videoSearch.getInsertTimeStart();
					videoSearch
							.setInsertTimeStart(videoSearch.getInsertTimeEnd());
					videoSearch.setInsertTimeEnd(temp);
				}
			}
		}
		PageDataResult pdr = new PageDataResult();
		PageHelper.startPage(page, limit);
		List<Video> prList = videoMapper.getVideos(videoSearch);
		// 获取分页查询后的数据
		PageInfo<Video> pageInfo = new PageInfo<>(prList);
		// 设置获取到的总记录数total：
		pdr.setTotals(Long.valueOf(pageInfo.getTotal()).intValue());

		pdr.setList(prList);
		return pdr;
	}



	@Override
	public int insert(Video video) {
		return videoMapper.insert(video);
	}

	@Override
	public int delete(Integer videoId) {
		return videoMapper.delete(videoId);
	}
}
