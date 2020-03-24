package com.reacheng.rc.service.impl;


import com.reacheng.rc.dao.AppRepository;
import com.reacheng.rc.entity.Application;
import com.reacheng.rc.service.ApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ApiServiceImpl implements ApiService {

    @Autowired
    private AppRepository appRepository;


    @Override
    public List<Application> selectAppList(String imei, String projectName ) {
        List<Application> appList = appRepository.selectAppList (imei, projectName);
        return  appList;
    }

    @Override
    public List<Application> selectAppList(String imei,String projectName ,String packageName) {
        List<Application> appList = appRepository.selectAppList(imei, projectName , packageName);
        return  appList;
    }




}
