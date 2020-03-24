package com.reacheng.rc.service;

import com.reacheng.rc.entity.AppLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

public interface AppLogService extends BaseService<AppLog,Integer> {

    AppLog findAppInfo (String imei ,String projectName ,String packageName,String versionCode );

    Page<AppLog> findAllLog(String key, String imei, String status, Integer currentPage, Integer pageSize , Sort sort) ;
}
