package com.reacheng.rc.service;


import com.reacheng.rc.entity.AppInfo;

public interface AppInfoService extends BaseService<AppInfo,Integer> {

    AppInfo findByName (String appName);

    AppInfo findByPackageName (String packageName);

}
