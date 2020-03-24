package com.legaoyi.message.ext.service.impl;

import org.springframework.stereotype.Service;

import com.legaoyi.platform.ext.service.impl.DefaultMongoExtendServiceImpl;

/**
 * @author gaoshengbo
 */
@Service("jtt0700MessageExtendService")
public class JTT0700MessageExtendServiceImpl extends DefaultMongoExtendServiceImpl {

    @Override
    protected String getEntityName() {
        return "jtt808_0700_msg";
    }

    @Override
    public Object get(Object id) throws Exception {
        return this.mongoService.get(getEntityName(), String.valueOf(id));
    }
}
