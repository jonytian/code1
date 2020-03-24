package com.legaoyi.message.ext.service.impl;

import org.springframework.stereotype.Service;

import com.legaoyi.platform.ext.service.impl.DefaultMongoExtendServiceImpl;

/**
 * @author gaoshengbo
 */
@Service("jtt0702MessageExtendService")
public class JTT0702MessageExtendServiceImpl extends DefaultMongoExtendServiceImpl {

    @Override
    protected String getEntityName() {
        return "jtt808_0702_msg_";
    }
}
