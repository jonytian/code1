package com.example.logsys.mapper;

import com.example.logsys.entity.Device;
import com.example.logsys.entity.DeviceSearch;
import com.example.logsys.entity.DeviceVo;
import com.example.logsys.entity.DeviceVoSearch;
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
