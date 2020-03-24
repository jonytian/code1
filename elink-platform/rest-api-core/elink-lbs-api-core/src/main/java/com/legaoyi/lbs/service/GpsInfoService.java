package com.legaoyi.lbs.service;

import java.util.Map;

/**
 * @author gaoshengbo
 */
public interface GpsInfoService {

    public Map<?, ?> getLastGps(String deviceId) throws Exception;

    // public Object query(String[] selects, String orderBy, boolean desc, int pageSize, int pageNo,
    // boolean countable, Map<String, Object> form) throws Exception;

    public Map<String, Object> simplify(Map<String, Object> condition) throws Exception;
}
