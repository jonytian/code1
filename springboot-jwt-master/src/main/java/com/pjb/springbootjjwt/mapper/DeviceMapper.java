package com.pjb.springbootjjwt.mapper;

import com.pjb.springbootjjwt.entity.Device;
import com.pjb.springbootjjwt.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author jinbin
 * @date 2018-07-08 20:44
 */
@Mapper
public interface DeviceMapper {
    Device findByImei(@Param("imei") String imei);
    Device findDeviceById(@Param("Id") Integer Id);
}
