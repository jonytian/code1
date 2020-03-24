package com.legaoyi.data.service;

import java.util.List;
import java.util.Map;

public interface DataCrawlerService {

    public void saveOilPrices(Map<String, Object> data);
    
    public Map<String,Object> getArea(String area);
    
    public void batchSavePlatenoLimitRule(String areaCode,List<?> list);
}
