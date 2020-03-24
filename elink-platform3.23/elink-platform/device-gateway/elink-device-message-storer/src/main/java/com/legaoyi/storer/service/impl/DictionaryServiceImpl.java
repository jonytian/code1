package com.legaoyi.storer.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.legaoyi.common.util.Constants;
import com.legaoyi.storer.dao.DictionaryDao;
import com.legaoyi.storer.service.DictionaryService;

@Service("dictionaryService")
public class DictionaryServiceImpl implements DictionaryService {

	@Autowired
	@Qualifier("dictionaryDao")
	private DictionaryDao dictionaryDao;

	@Override
	@Cacheable(value = Constants.CACHE_NAME_DICTIONARY_CACHE, key = "'ftp_server'", unless = "#result == null")
	public Map<String, Object> getFtpServer() throws Exception {
		return dictionaryDao.getFtpServer();
	}

	@Override
	@Cacheable(value = Constants.CACHE_NAME_DICTIONARY_CACHE, key = "'1078-video-server'", unless = "#result == null")
	public List<Map<String, Object>> get1078Servers() throws Exception {
		return dictionaryDao.get1078Servers();
	}

	@Override
	@Cacheable(value = Constants.CACHE_NAME_DICTIONARY_CACHE, key = "'1078-video-stats'", unless = "#result == null")
	public String getMediaStatsUrl() throws Exception {
		return dictionaryDao.getMediaStatsUrl();
	}

}
