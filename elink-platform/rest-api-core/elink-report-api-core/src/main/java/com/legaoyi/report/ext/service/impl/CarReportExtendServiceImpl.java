package com.legaoyi.report.ext.service.impl;

import org.springframework.stereotype.Service;

import com.legaoyi.platform.ext.service.impl.DefaultMongoExtendServiceImpl;

/**
 * @author gaoshengbo
 */
@Service("carReportExtendService")
public class CarReportExtendServiceImpl extends DefaultMongoExtendServiceImpl {

    @Override
    protected String getEntityName() {
        return "car_report";
    }

    @Override
    public Object get(Object id) throws Exception {
        return this.mongoService.get(getEntityName(),(String)id);
    }
}
