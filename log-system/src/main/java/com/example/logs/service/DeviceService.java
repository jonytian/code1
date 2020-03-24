package com.example.logs.service;


import com.example.logs.entity.Device;
import com.example.logs.entity.DeviceSearch;
import com.example.logs.util.PageDataResult;

/**
 * Created by tyj on 2019/08/15.
 */
public interface DeviceService {

	/**
	 * 分页查询设备列表
	 * @param page
	 * @param limit
	 * @return
	 */
	PageDataResult getDevices(DeviceSearch deviceSearch, int page, int limit);

    int insertDevices(Device device);

	int update(Device device);

	 Device findByImei(String imei);
}
