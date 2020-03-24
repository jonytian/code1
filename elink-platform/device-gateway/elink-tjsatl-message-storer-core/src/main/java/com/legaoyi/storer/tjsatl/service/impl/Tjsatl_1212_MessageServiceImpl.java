package com.legaoyi.storer.tjsatl.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.legaoyi.storer.dao.GeneralDao;
import com.legaoyi.storer.service.GeneralService;
import com.legaoyi.storer.util.Constants;

@Transactional
@Service(Constants.ELINK_MESSAGE_STORER_BEAN_PREFIX + "1212" + Constants.ELINK_MESSAGE_STORER_MESSAGE_SERVICE_BEAN_SUFFIX)
public class Tjsatl_1212_MessageServiceImpl implements GeneralService {

    @Autowired
    @Qualifier(Constants.ELINK_MESSAGE_STORER_BEAN_PREFIX + "0801" + Constants.ELINK_MESSAGE_STORER_MESSAGE_DAO_BEAN_SUFFIX)
    private GeneralDao mediaDataDao;
    
    @Autowired
    @Qualifier("deviceUpMessageDao")
    private GeneralDao deviceUpMessageDao;

    @Override
    public void batchSave(List<?> list) throws Exception {
        mediaDataDao.batchSave(list);
        deviceUpMessageDao.batchSave(list);
    }
}
