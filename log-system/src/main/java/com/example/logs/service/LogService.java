package com.example.logs.service;


import com.example.logs.entity.Log;
import com.example.logs.entity.LogVoSearch;
import com.example.logs.util.PageDataResult;

/**
 * Created by tyj on 2019/08/15.
 */
public interface LogService {

	/**
	 * 分页查询日志列表
	 * @param page
	 * @param limit
	 * @return
	 */
	PageDataResult getLogs(LogVoSearch logVoSearch, int page, int limit);

    int insertLogs(Log log);

	int update(Log log);

	Log getOne(String exceptionName, String happenTime);
}
