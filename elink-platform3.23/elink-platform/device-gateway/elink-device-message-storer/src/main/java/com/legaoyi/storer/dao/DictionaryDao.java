package com.legaoyi.storer.dao;

import java.util.List;
import java.util.Map;

public interface DictionaryDao {

    public Map<String, Object> getFtpServer() throws Exception;
    
    public List<Map<String, Object>> get1078Servers() throws Exception;
    
    public String getMediaStatsUrl() throws Exception;
    
}
