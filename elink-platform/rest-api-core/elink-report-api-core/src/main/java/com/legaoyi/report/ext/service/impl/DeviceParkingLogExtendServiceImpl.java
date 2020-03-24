package com.legaoyi.report.ext.service.impl;

import org.springframework.stereotype.Service;

import com.legaoyi.platform.ext.service.impl.DefaultMongoExtendServiceImpl;

/**
 * @author gaoshengbo
 */
@Service("deviceParkingLogExtendService")
public class DeviceParkingLogExtendServiceImpl extends DefaultMongoExtendServiceImpl {

    @Override
    protected String getEntityName() {
        return "device_parking_logs_";
    }
}
