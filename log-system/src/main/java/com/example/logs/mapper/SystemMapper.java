package com.example.logs.mapper;

import com.example.logs.entity.SystemLog;
import org.apache.ibatis.annotations.Mapper;


@Mapper
public interface SystemMapper {

    int insert(SystemLog systemLog);

}
