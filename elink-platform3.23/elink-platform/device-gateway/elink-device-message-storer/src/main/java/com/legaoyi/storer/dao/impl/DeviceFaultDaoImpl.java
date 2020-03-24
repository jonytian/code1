package com.legaoyi.storer.dao.impl;

import java.util.List;
import com.legaoyi.storer.dao.MongoDao;
import com.legaoyi.common.util.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.legaoyi.storer.dao.GeneralDao;

@Component("deviceFaultDao")
public class DeviceFaultDaoImpl implements GeneralDao {

    private static final Logger logger = LoggerFactory.getLogger(DeviceFaultDaoImpl.class);

    @SuppressWarnings("rawtypes")
    @Autowired
    @Qualifier("mongoDao")
    private MongoDao mongoDao;

    @SuppressWarnings("unchecked")
    @Override
    public void batchSave(final List<?> list) throws Exception {
        try {
            mongoDao.batchSave(list, "device_fault");
        } catch (Exception e) {
            logger.error("batch save mongoDB error,data={}", JsonUtil.covertObjectToString(list), e);
        }
    }
}
