package com.legaoyi.storer.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.legaoyi.storer.dao.GeneralDao;
import com.legaoyi.storer.service.GeneralService;

@Service("deviceFaultService")
public class DeviceFaultServiceImpl implements GeneralService {

    @Autowired
    @Qualifier("deviceFaultDao")
    private GeneralDao deviceFaultDao;

    @Override
    public void batchSave(List<?> list) throws Exception {
        deviceFaultDao.batchSave(list);
    }

}
