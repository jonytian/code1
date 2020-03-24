package com.reacheng.rc.service.impl;


import com.reacheng.rc.dao.AppInfoRepository;
import com.reacheng.rc.entity.AppInfo;
import com.reacheng.rc.service.AppInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("appInfoService")
public class AppInfoServiceImpl extends BaseServiceImpl <AppInfo, Integer> implements AppInfoService {

    @Autowired
    AppInfoRepository appInfoRepository;

    @Override
    public AppInfo findByName(String appName) {
        return appInfoRepository.findByName(appName);
    }

    @Override
    public AppInfo findByPackageName(String packageName) {
        return appInfoRepository.findByPackageName(packageName);
    }
}
