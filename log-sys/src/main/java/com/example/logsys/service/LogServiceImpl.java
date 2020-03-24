package com.example.logsys.service;



import com.example.logsys.entity.*;
import com.example.logsys.mapper.DeviceMapper;
import com.example.logsys.mapper.LogMapper;
import com.example.logsys.util.PageDataResult;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by tyj on 2019/08/15.
 */
@Service
@Slf4j
public class LogServiceImpl implements LogService {

	@Autowired
	private LogMapper logMapper;

	@Autowired
	private DeviceMapper deviceMapper;


	@Override
	public PageDataResult getLogs(LogVoSearch logVoSearch, int page, int limit) {
		// 时间处理
//		if (null != userSearch) {
//			if (StringUtils.isNotEmpty(userSearch.getInsertTimeStart())
//					&& StringUtils.isEmpty(userSearch.getInsertTimeEnd())) {
//				userSearch.setInsertTimeEnd(DateUtil.format(new Date()));
//			} else if (StringUtils.isEmpty(userSearch.getInsertTimeStart())
//					&& StringUtils.isNotEmpty(userSearch.getInsertTimeEnd())) {
//				userSearch.setInsertTimeStart(DateUtil.format(new Date()));
//			}
//			if (StringUtils.isNotEmpty(userSearch.getInsertTimeStart())
//					&& StringUtils.isNotEmpty(userSearch.getInsertTimeEnd())) {
//				if (userSearch.getInsertTimeEnd().compareTo(
//						userSearch.getInsertTimeStart()) < 0) {
//					String temp = userSearch.getInsertTimeStart();
//					userSearch
//							.setInsertTimeStart(userSearch.getInsertTimeEnd());
//					userSearch.setInsertTimeEnd(temp);
//				}
//			}
//		}
		PageDataResult pdr = new PageDataResult();
		PageHelper.startPage(page, limit);
		List<LogVo> prList = logMapper.selectAllLogs(logVoSearch);
		// 获取分页查询后的数据
		PageInfo<LogVo> pageInfo = new PageInfo<>(prList);
		// 设置获取到的总记录数total：
		pdr.setTotals(Long.valueOf(pageInfo.getTotal()).intValue());

		pdr.setList(prList);
		return pdr;
	}

    @Override
    public int insertLogs(Log log) {
        return logMapper.insert(log);
    }

	@Override
	public int update(Log log) {
		return logMapper.update(log);
	}

	@Override
	public Log getOne(String exceptionName, String happenTime) {
		return logMapper.getOne(exceptionName,happenTime);
	}


}
