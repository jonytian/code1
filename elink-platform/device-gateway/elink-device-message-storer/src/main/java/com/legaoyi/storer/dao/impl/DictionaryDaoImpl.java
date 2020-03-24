package com.legaoyi.storer.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.legaoyi.common.util.JsonUtil;
import com.legaoyi.storer.dao.DictionaryDao;

@Component("dictionaryDao")
public class DictionaryDaoImpl implements DictionaryDao {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> getFtpServer() throws Exception {
		String sql = "select content from system_config_dictionary where type= ? and code = ? limit 1";
		Map<String, Object> data = jdbcTemplate.queryForMap(sql, 98, "ftp_server");
		if (data != null) {
			return JsonUtil.convertStringToObject((String) data.get("content"), Map.class);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, Object>> get1078Servers() throws Exception {
		List<Map<String, Object>> retList = new ArrayList<Map<String, Object>>();
		String sql = "select content from system_config_dictionary where type= ? and code = ?";
		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql, 99, "1");
		for (Map<String, Object> map : list) {
			Map<String, Object> data = JsonUtil.convertStringToObject((String) map.get("content"), Map.class);
			retList.add(data);
		}
		return retList;
	}

	@Override
	public String getMediaStatsUrl() throws Exception {
		String sql = "select content from system_config_dictionary where type= ? and code = ? limit 1";
		Map<String, Object> data = jdbcTemplate.queryForMap(sql, 90, "1078-video-stats");
		if (data != null) {
			Map<?, ?> map = JsonUtil.convertStringToObject((String) data.get("content"), Map.class);
			return (String) map.get("url");
		}
		return null;
	}

}
