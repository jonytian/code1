package com.example.sys.service;

import com.example.sys.entity.HmVideo;
import com.example.sys.entity.HmVideoSearch;
import com.example.sys.entity.Video;
import com.example.sys.mapper.HmVideoMapper;
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
public class HmVideoServiceImpl implements HmVideoService {

	@Autowired
	private HmVideoMapper hmVideoMapper;


	@Override
	public PageDataResult findAll(HmVideoSearch hmVideoSearch, int page, int limit) {
		// 时间处理
		if (null != hmVideoSearch) {
			if (StringUtils.isNotEmpty(hmVideoSearch.getInsertTimeStart())
					&& StringUtils.isEmpty(hmVideoSearch.getInsertTimeEnd())) {
				hmVideoSearch.setInsertTimeEnd(DateUtils.format(new Date()));
			} else if (StringUtils.isEmpty(hmVideoSearch.getInsertTimeStart())
					&& StringUtils.isNotEmpty(hmVideoSearch.getInsertTimeEnd())) {
				hmVideoSearch.setInsertTimeStart(DateUtils.format(new Date()));
			}
			if (StringUtils.isNotEmpty(hmVideoSearch.getInsertTimeStart())
					&& StringUtils.isNotEmpty(hmVideoSearch.getInsertTimeEnd())) {
				if (hmVideoSearch.getInsertTimeEnd().compareTo(
						hmVideoSearch.getInsertTimeStart()) < 0) {
					String temp = hmVideoSearch.getInsertTimeStart();
					hmVideoSearch
							.setInsertTimeStart(hmVideoSearch.getInsertTimeEnd());
					hmVideoSearch.setInsertTimeEnd(temp);
				}
			}
		}
		PageDataResult pdr = new PageDataResult();
		PageHelper.startPage(page, limit);
		List<Video> prList = hmVideoMapper.findAll(hmVideoSearch);
		// 获取分页查询后的数据
		PageInfo<Video> pageInfo = new PageInfo<>(prList);
		// 设置获取到的总记录数total：
		pdr.setTotals(Long.valueOf(pageInfo.getTotal()).intValue());

		pdr.setList(prList);
		return pdr;
	}



	@Override
	public int insert(HmVideo hmVideo) {
		return hmVideoMapper.insert(hmVideo);
	}

	@Override
	public int delete(Integer videoId) {
		return hmVideoMapper.delete(videoId);
	}
}
