package com.example.logsys.mapper;

import com.example.logsys.entity.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface LogMapper {

    int insert(Log log);

    int update(Log log);
    /**
     * 分页查询日志数据
     * @return
     */
    List<LogVo> selectAllLogs(@Param("logVoSearch") LogVoSearch logVoSearch);

    Log getOne (String exceptionName , String happenTime );
}
