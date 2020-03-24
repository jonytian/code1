package com.example.logs.service;



import com.example.logs.entity.SystemLog;
import com.example.logs.mapper.SystemMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
