package com.legaoyi.storer.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.legaoyi.storer.dao.DeviceDownMessageDao;
import com.legaoyi.storer.service.DeviceDownMessageService;

@Service("deviceDownMessageService")
public class DeviceDownMessageServiceImpl implements DeviceDownMessageService {

    @Autowired
    @Qualifier("deviceDownMessageDao")
    private DeviceDownMessageDao deviceDownMessageDao;

    @Override
    public void batchSave(List<?> list) throws Exception {
        deviceDownMessageDao.batchSave(list);
    }

    @Override
    public void setMessageState(int state, String id) throws Exception {
        deviceDownMessageDao.setMessageState(state, id);
    }
}
