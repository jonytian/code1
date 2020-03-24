package com.legaoyi.storer.dao.impl;

import java.util.Date;
import java.util.List;

import com.legaoyi.storer.dao.MongoDao;
import com.legaoyi.common.util.JsonUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.legaoyi.storer.dao.GeneralDao;
import com.legaoyi.storer.util.Constants;

@Component(Constants.ELINK_MESSAGE_STORER_BEAN_PREFIX + "unknown" + Constants.ELINK_MESSAGE_STORER_MESSAGE_DAO_BEAN_SUFFIX)
public class JTT808_Unknown_MessageDaoImpl implements GeneralDao {

    private static final Logger logger = LoggerFactory.getLogger(JTT808_Unknown_MessageDaoImpl.class);

    @SuppressWarnings("rawtypes")
    @Autowired
    @Qualifier("mongoDao")
    private MongoDao mongoDao;

    @SuppressWarnings("unchecked")
    @Override
    public void batchSave(final List<?> list) throws Exception {
        try {
            mongoDao.batchInsert(list, "jtt808_unknown_msg_".concat(new java.text.SimpleDateFormat("yyyyMMdd").format(new Date())));
        } catch (Exception e) {
            logger.error("batch save mongoDB error,data={}", JsonUtil.covertObjectToString(list), e);
        }
    }
}
