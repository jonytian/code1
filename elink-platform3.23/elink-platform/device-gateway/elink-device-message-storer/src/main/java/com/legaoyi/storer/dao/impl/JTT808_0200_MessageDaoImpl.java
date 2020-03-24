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
import com.legaoyi.storer.dao.GpsInfoDao;
import com.legaoyi.storer.util.Constants;

@Component(Constants.ELINK_MESSAGE_STORER_BEAN_PREFIX + "0200" + Constants.ELINK_MESSAGE_STORER_MESSAGE_DAO_BEAN_SUFFIX)
public class JTT808_0200_MessageDaoImpl implements GpsInfoDao {

    private static final Logger logger = LoggerFactory.getLogger(JTT808_0200_MessageDaoImpl.class);

    @SuppressWarnings("rawtypes")
    @Autowired
    @Qualifier("mongoDao")
    private MongoDao mongoDao;

    @Override
    public void batchSave(final List<?> list) throws Exception {
        batchSave(list, new java.text.SimpleDateFormat("yyyyMMdd").format(new Date()));
    }

    @SuppressWarnings("unchecked")
    @Override
    public void batchSave(final List<?> list, String date) throws Exception {
        try {
            // 位置信息按天存储mongodb
            mongoDao.batchInsert(list, "gps_info_".concat(date));
        } catch (Exception e) {
            logger.error("batch save mongoDB error,data={}", JsonUtil.covertObjectToString(list), e);
        }
    }
}
