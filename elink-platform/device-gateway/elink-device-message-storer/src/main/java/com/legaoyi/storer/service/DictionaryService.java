package com.legaoyi.storer.service;

import java.util.List;
import java.util.Map;

public interface DictionaryService {

	public Map<String, Object> getFtpServer() throws Exception;
	
	public List<Map<String, Object>> get1078Servers() throws Exception;

	public String getMediaStatsUrl() throws Exception;
}
