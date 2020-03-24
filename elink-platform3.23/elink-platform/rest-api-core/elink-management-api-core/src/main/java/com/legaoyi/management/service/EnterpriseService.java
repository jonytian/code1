package com.legaoyi.management.service;

import java.util.Map;
import com.legaoyi.platform.model.User;

public interface EnterpriseService {

    public User register(Map<String, Object> entity) throws Exception;
}
