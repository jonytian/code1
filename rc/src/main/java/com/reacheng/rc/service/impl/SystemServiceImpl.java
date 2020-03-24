package com.reacheng.rc.service.impl;

import com.reacheng.rc.entity.Systems;
import com.reacheng.rc.service.SystemService;
import org.springframework.stereotype.Service;

@Service(value = "systemService")
public class SystemServiceImpl extends BaseServiceImpl <Systems, Integer> implements SystemService {
}
