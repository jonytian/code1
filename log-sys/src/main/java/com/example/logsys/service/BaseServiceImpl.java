package com.example.logsys.service;


import com.example.logsys.entity.Log;
import com.example.logsys.entity.LogVo;
import com.example.logsys.entity.LogVoSearch;
import com.example.logsys.entity.SystemLog;
import com.example.logsys.mapper.DeviceMapper;
import com.example.logsys.mapper.LogMapper;
import com.example.logsys.mapper.SystemMapper;
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
public class BaseServiceImpl implements BaseService {

	@Autowired
	private SystemMapper systemMapper;

	@Override
	public int save(SystemLog log) {
		return systemMapper.insert(log);
	}
}
