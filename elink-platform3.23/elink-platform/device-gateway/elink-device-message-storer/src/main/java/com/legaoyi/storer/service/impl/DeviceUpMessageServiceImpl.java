package com.legaoyi.storer.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.legaoyi.storer.dao.GeneralDao;
import com.legaoyi.storer.service.GeneralService;

@Transactional
@Service("deviceUpMessageService")
public class DeviceUpMessageServiceImpl implements GeneralService {

    @Autowired
    @Qualifier("deviceUpMessageDao")
    private GeneralDao deviceUpMessageDao;

    public void batchSave(List<?> list) throws Exception {
        deviceUpMessageDao.batchSave(list);
    }

}
