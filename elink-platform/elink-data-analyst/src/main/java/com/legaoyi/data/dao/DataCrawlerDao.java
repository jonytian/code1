package com.legaoyi.data.dao;

import java.util.List;
import java.util.Map;

public interface DataCrawlerDao {

    public void saveOilPrices(Map<String, Object> data);

    public Map<String, Object> getArea(String area);

    public void batchSavePlatenoLimitRule(List<?> list);

    public void delPlatenoLimitRule(String areaCode);

}
