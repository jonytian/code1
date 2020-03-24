package com.legaoyi.storer.tjsatl.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.legaoyi.common.util.JsonUtil;
import com.legaoyi.storer.tjsatl.dao.FileServerDao;
import com.legaoyi.storer.tjsatl.util.Constants;

@Component("fileServerDao")
public class FileServerDaoImpl implements FileServerDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @SuppressWarnings("unchecked")
    @Override
    public List<?> getFileServer() throws Exception {
        List<Map<String, Object>> ret = new ArrayList<Map<String, Object>>();
        String sql = "select content from system_config_dictionary where type= ? and code = ?";
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql, Constants.DICTIONARY_TYPE_TJSATL_FILE_SERVER, Constants.DICTIONARY_KEY_TJSATL_FILE_SERVER);
        if (list != null && !list.isEmpty()) {
            for (Map<String, Object> map : list) {
                ret.add(JsonUtil.convertStringToObject((String) map.get("content"), Map.class));
            }
        }
        return ret;
    }

}
