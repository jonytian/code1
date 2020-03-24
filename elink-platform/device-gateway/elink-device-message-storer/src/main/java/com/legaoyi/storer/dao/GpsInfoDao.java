package com.legaoyi.storer.dao;

import java.util.List;

public interface GpsInfoDao extends GeneralDao{
    
    public void batchSave(final List<?> list,String date) throws Exception;

}
