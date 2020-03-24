package com.legaoyi.storer.service;

import java.util.Map;

public interface ConfigService {

    public Map<String, Object> getEnterpriseConfig(String enterpriseId) throws Exception;
}
