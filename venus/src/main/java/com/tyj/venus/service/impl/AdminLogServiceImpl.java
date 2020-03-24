package com.tyj.venus.service.impl;


import com.tyj.venus.entity.AdminLog;
import com.tyj.venus.service.AdminLogService;
import org.springframework.stereotype.Service;

@Service(value = "adminLogService")
public class AdminLogServiceImpl extends BaseServiceImpl <AdminLog, Integer> implements AdminLogService {
}
