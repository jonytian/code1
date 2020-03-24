package com.tyj.venus.service.impl;


import com.tyj.venus.entity.Systems;
import com.tyj.venus.service.SystemService;
import org.springframework.stereotype.Service;

@Service(value = "systemService")
public class SystemServiceImpl extends BaseServiceImpl <Systems, Integer> implements SystemService {
}
