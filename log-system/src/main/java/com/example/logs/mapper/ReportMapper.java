package com.example.logs.mapper;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface ReportMapper {

    // 折线
    List<Map<String, Object>> getChartLine();
    // 柱状
    List<Map<String, Object>> getChartBar();
    // 饼
    List<Map<String, Object>> getChartPie();

    // 获取项目的分组
    List<Map<String, Object>> getProjectGroup();



    // 设备
    int getDeviceAllTotal();

    int getDeviceDay();

    int getDeviceMonth();

    int getDeviceYear();

    int getDeviceWeek();

    //用户
    int getUserAllTotal();

    int getUserDay();

    int getUserMonth();

    int getUserYear();

    int getUserWeek();

    // 日志
    int getExceptionAllTotal();

    int getExceptionDay();

    int getExceptionMonth();

    int getExceptionYear();

    int getExceptionWeek();


}
