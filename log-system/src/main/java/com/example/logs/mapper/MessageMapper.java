package com.example.logs.mapper;


import com.example.logs.entity.DeviceVo;
import com.example.logs.entity.Message;
import com.example.logs.entity.MessageSearch;
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
