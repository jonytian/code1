package com.legaoyi.message.ext.service.impl;

import org.springframework.stereotype.Service;

import com.legaoyi.platform.ext.service.impl.DefaultMongoExtendServiceImpl;

/**
 * @author gaoshengbo
 */
@Service("onlineLogExtendService")
public class OnlineLogExtendServiceImpl extends DefaultMongoExtendServiceImpl {

    @Override
    protected String getEntityName() {
        return "on_offline_logs_";
    }
}
