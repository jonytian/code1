package com.reacheng.rc.service.impl;

import com.reacheng.rc.entity.AdminLog;
import com.reacheng.rc.service.AdminLogService;
import org.springframework.stereotype.Service;

@Service(value = "adminLogService")
public class AdminLogServiceImpl extends BaseServiceImpl <AdminLog, Integer> implements AdminLogService {
}
