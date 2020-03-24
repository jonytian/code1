package com.legaoyi.storer.dao;

import java.util.List;

public interface DeviceUpMessageDao extends GeneralDao {

    public void batchUpdate(final List<?> list);

    public void updateFileUploadState(String deviceId, int respMessageSeq, int state, int messageSeq);
}
