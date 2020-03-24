package com.legaoyi.report.ext.service.impl;

import org.springframework.stereotype.Service;

import com.legaoyi.platform.ext.service.impl.DefaultMongoExtendServiceImpl;

/**
 * @author gaoshengbo
 */
@Service("attendanceDayReportExtendService")
public class AttendanceDayReportExtendServiceImpl extends DefaultMongoExtendServiceImpl {

    @Override
    protected String getEntityName() {
        return "attendance_day_report_";
    }

    @Override
    public Object get(Object id) throws Exception {
        String idStr = String.valueOf(id);
        return this.mongoService.get(getEntityName().concat(idStr.split("_")[0]), idStr);
    }
}
