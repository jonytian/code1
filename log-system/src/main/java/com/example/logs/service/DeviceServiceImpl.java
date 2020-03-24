package com.example.logs.service;

import com.example.logs.entity.Device;
import com.example.logs.entity.DeviceSearch;
import com.example.logs.mapper.DeviceMapper;
import com.example.logs.util.PageDataResult;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by tyj on 2019/08/15.
 */
@Service
@Slf4j
public class DeviceServiceImpl implements DeviceService {

	@Autowired
	private DeviceMapper deviceMapper;


	@Override
	public PageDataResult getDevices(DeviceSearch deviceSearch, int page, int limit) {
		// 时间处理
//		if (null != userSearch) {
//			if (StringUtils.isNotEmpty(userSearch.getInsertTimeStart())
//					&& StringUtils.isEmpty(userSearch.getInsertTimeEnd())) {
//				userSearch.setInsertTimeEnd(DateUtil.format(new Date()));
//			} else if (StringUtils.isEmpty(userSearch.getInsertTimeStart())
//					&& StringUtils.isNotEmpty(userSearch.getInsertTimeEnd())) {
//				userSearch.setInsertTimeStart(DateUtil.format(new Date()));
//			}
//			if (StringUtils.isNotEmpty(userSearch.getInsertTimeStart())
//					&& StringUtils.isNotEmpty(userSearch.getInsertTimeEnd())) {
//				if (userSearch.getInsertTimeEnd().compareTo(
//						userSearch.getInsertTimeStart()) < 0) {
//					String temp = userSearch.getInsertTimeStart();
//					userSearch
//							.setInsertTimeStart(userSearch.getInsertTimeEnd());
//					userSearch.setInsertTimeEnd(temp);
//				}
//			}
//		}
		PageDataResult pdr = new PageDataResult();
		PageHelper.startPage(page, limit);
		List<Device> prList = deviceMapper.getDevices(deviceSearch);
		// 获取分页查询后的数据
		PageInfo<Device> pageInfo = new PageInfo<>(prList);
		// 设置获取到的总记录数total：
		pdr.setTotals(Long.valueOf(pageInfo.getTotal()).intValue());

		pdr.setList(prList);
		return pdr;
	}

    @Override
    public int insertDevices(Device device) {
        return deviceMapper.insert(device);
    }

	@Override
	public int update(Device device) {
		return deviceMapper.update(device);
	}

	@Override
	public Device findByImei(String imei) {
		return deviceMapper.findByImei(imei);
	}


}
