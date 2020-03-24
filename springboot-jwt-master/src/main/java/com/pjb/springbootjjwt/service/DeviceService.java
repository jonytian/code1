package com.pjb.springbootjjwt.service;

import com.pjb.springbootjjwt.entity.Device;
import com.pjb.springbootjjwt.entity.User;
import com.pjb.springbootjjwt.mapper.DeviceMapper;
import com.pjb.springbootjjwt.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author jinbin
 * @date 2018-07-08 20:52
 */
@Service("deviceService")
public class DeviceService {
    @Autowired
    DeviceMapper deviceMapper;
    public Device findByImei(Device device){
        return deviceMapper.findByImei(device.getImei());
    }
    public Device findDeviceById(Integer userId) {
        return deviceMapper.findDeviceById(userId);
    }

}
