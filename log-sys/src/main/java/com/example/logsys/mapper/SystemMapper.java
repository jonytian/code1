package com.example.logsys.mapper;

import com.example.logsys.entity.SystemLog;
import org.apache.ibatis.annotations.Mapper;


@Mapper
public interface SystemMapper {

    int insert(SystemLog systemLog);

}
