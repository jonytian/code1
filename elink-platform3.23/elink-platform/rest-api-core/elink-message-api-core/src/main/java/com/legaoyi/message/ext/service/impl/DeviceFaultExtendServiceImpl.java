package com.legaoyi.message.ext.service.impl;

import org.springframework.stereotype.Service;

import com.legaoyi.platform.ext.service.impl.DefaultMongoExtendServiceImpl;

/**
 * @author gaoshengbo
 */
@Service("deviceFaultExtendService")
public class DeviceFaultExtendServiceImpl extends DefaultMongoExtendServiceImpl {

    @Override
    protected String getEntityName() {
        return "device_fault";
    }

    @Override
    public Object get(Object id) throws Exception {
        return this.mongoService.get(getEntityName(), String.valueOf(id));
    }
}
