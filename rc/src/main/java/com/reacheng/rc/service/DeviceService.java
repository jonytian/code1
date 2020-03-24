package com.reacheng.rc.service;


import com.reacheng.rc.entity.Device;
import com.reacheng.rc.entity.Project;
import org.springframework.data.domain.Page;

import java.util.List;


public interface DeviceService extends BaseService<Device,Integer> {

    Device findByImei (String imei);

    Device findByImeiAndProject (String imei,Project project);

    Page<Device> findAllDev(String key, String projectName, Integer currentPage, Integer pageSize) ;

    //查询项目下所有设备
    List<Device> findAllDevice(Project project);
}
