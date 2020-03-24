package com.reacheng.rc.service;



import com.reacheng.rc.entity.Application;

import java.util.List;

public interface ApiService {

    /** 查询app列表*/
    public List<Application> selectAppList(String imei, String projectName);

    public List<Application> selectAppList(String imei, String projectName, String packageName);



}
