package com.legaoyi.storer.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.legaoyi.common.util.Constants;
import com.legaoyi.storer.dao.DeviceDao;
import com.legaoyi.storer.service.ConfigService;

@Service("configService")
public class ConfigServiceImpl implements ConfigService {

    @Autowired
    @Qualifier("deviceDao")
    private DeviceDao deviceDao;

    @Override
    @Cacheable(value = Constants.CACHE_NAME_ENTERPRISE_CONFIG_CACHE, key = "#enterpriseId")
    public Map<String, Object> getEnterpriseConfig(String enterpriseId) throws Exception {
        Map<String, Object> map = deviceDao.getEnterpriseConfig(enterpriseId);
        if (map == null) {
            map = new HashMap<String, Object>();
        }
        return map;
    }
}
