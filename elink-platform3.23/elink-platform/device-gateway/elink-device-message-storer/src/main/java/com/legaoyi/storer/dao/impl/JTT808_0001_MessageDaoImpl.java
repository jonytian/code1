package com.legaoyi.storer.dao.impl;

import java.util.List;

import com.legaoyi.common.util.JsonUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.legaoyi.storer.util.Constants;
import com.legaoyi.storer.dao.GeneralDao;
import com.legaoyi.storer.dao.MongoDao;

@Component(Constants.ELINK_MESSAGE_STORER_BEAN_PREFIX + "0001" + Constants.ELINK_MESSAGE_STORER_MESSAGE_DAO_BEAN_SUFFIX)
public class JTT808_0001_MessageDaoImpl implements GeneralDao {

    private static final Logger logger = LoggerFactory.getLogger(JTT808_0001_MessageDaoImpl.class);

    @SuppressWarnings("rawtypes")
    @Autowired
    @Qualifier("mongoDao")
    private MongoDao mongoDao;

    @SuppressWarnings("unchecked")
    @Override
    public void batchSave(final List<?> list) throws Exception {
        try {
            mongoDao.batchInsert(list, "jtt808_0001_msg");
        } catch (Exception e) {
            logger.error("batch save mongoDB error,data={}", JsonUtil.covertObjectToString(list), e);
        }
    }
}
