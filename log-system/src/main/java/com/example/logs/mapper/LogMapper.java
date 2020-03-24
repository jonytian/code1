package com.example.logs.mapper;

import com.example.logs.entity.Log;
import com.example.logs.entity.LogVo;
import com.example.logs.entity.LogVoSearch;
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

    Log getOne(String exceptionName, String happenTime);
}
