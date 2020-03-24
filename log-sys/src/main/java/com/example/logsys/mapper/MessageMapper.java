package com.example.logsys.mapper;

import com.example.logsys.entity.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface MessageMapper {

    int insert(Message message);
    /**
     * 分页查询设备数据
     * @return
     */
    List<Message> getMessages(@Param("messageSearch") MessageSearch messageSearch);


    /**
     * 查询所有DeviceVo
     * @return
     */
    List<DeviceVo> fianAllMessages(@Param("messageSearch") MessageSearch messageSearch);
}
