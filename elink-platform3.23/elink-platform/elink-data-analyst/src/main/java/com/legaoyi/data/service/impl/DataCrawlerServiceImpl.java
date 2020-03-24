package com.legaoyi.data.service.impl;

import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.legaoyi.data.dao.DataCrawlerDao;
import com.legaoyi.data.service.DataCrawlerService;

@Service("dataCrawlerService")
public class DataCrawlerServiceImpl implements DataCrawlerService {

    @Autowired
    private DataCrawlerDao dataCrawlerDao;

    @Override
    public void saveOilPrices(Map<String, Object> data) {
        dataCrawlerDao.saveOilPrices(data);
    }

    @Override
    public Map<String, Object> getArea(String area) {
        return dataCrawlerDao.getArea(area);
    }

    @Override
    @Transactional
    public void batchSavePlatenoLimitRule(String areaCode, List<?> list) {
        dataCrawlerDao.delPlatenoLimitRule(areaCode);
        dataCrawlerDao.batchSavePlatenoLimitRule(list);
    }

}
