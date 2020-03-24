package com.example.logs.mapper;


import com.example.logs.entity.Device;
import com.example.logs.entity.DeviceSearch;
import com.example.logs.entity.DeviceVo;
import com.example.logs.entity.DeviceVoSearch;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DeviceMapper {

    int insert(Device device);

    int update(Device device);

    /**
     * 分页查询设备数据
     * @return
     */
    List<Device> getDevices(@Param("deviceSearch") DeviceSearch deviceSearch);


    /**
     * 查询所有DeviceVo
     * @return
     */
    List<DeviceVo> selectAllDeviceVo(@Param("deviceVoSearch") DeviceVoSearch deviceVoSearch);


    Device findByImei(@Param("imei") String imei);
}
