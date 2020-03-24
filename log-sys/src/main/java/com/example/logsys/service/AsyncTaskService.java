package com.example.logsys.service;


import com.example.logsys.entity.BusClick;
import com.example.logsys.entity.Device;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;


@Slf4j
@Service
public class AsyncTaskService {

    @Autowired
    private DeviceService deviceService;


    public void executeAsyncTask(Map map) {
        if(map.isEmpty()){return;}
        Device device = new Device();
        if(map.containsKey("imei")){
            device.setImei((String) map.get("imei"));
        }
        if(map.containsKey("projectName")){
            device.setProjectName((String) map.get("projectName"));
        }
        if(map.containsKey("code")){
            device.setCode((String) map.get("code"));
        }
        device.setConnectTime(new Date());
        device.setCreateTime(new Date());
        int b = deviceService.insertDevices(device);
        if (b == 1){
            log.info("**************新增设备成功！*********************");
        }else {
            log.info("**************新增设备失败！*********************");
        }

    }




}
