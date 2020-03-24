package com.legaoyi.message.ext.service.impl;

import org.springframework.stereotype.Service;

import com.legaoyi.platform.ext.service.impl.DefaultMongoExtendServiceImpl;

/**
 * @author gaoshengbo
 */
@Service("accStateLogExtendService")
public class AccStateLogExtendServiceImpl extends DefaultMongoExtendServiceImpl {

    @Override
    protected String getEntityName() {
        return "acc_state_logs_";
    }
}
