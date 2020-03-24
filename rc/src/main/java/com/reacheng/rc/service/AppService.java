package com.reacheng.rc.service;

import com.reacheng.rc.entity.AppLog;
import com.reacheng.rc.entity.Application;
import com.reacheng.rc.entity.Project;
import org.springframework.data.domain.Page;

import java.util.List;

public interface AppService extends BaseService<Application,Integer> {

  List<Application> findByProjectName  (String projectName);

  List<Application> findByProjectNameAndStatus  (String projectName,String handleStatus);

  List<Application> findByAll (String packageName,String projectName,String imei);

  List<Application> findByAll (String projectName,String imei);

 Application findByPackageNameAndProjectNameAndVersion (String packageName, Integer projectId, String version);

    Page<Application> findAllApp(String key, String projectName,String status, Integer currentPage, Integer pageSize) ;
}
