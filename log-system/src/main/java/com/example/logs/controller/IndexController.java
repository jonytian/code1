package com.example.logs.controller;

import com.alibaba.fastjson.JSON;
import com.example.logs.mapper.ReportMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Controller
@RequestMapping("/index")
public class IndexController extends BaseController{

    @Autowired
    private ReportMapper reportMapper;



    @GetMapping("/getTotal")
    @ResponseBody
    public String getTotal() {
        Map resultMap = new HashMap(16);
        List result = new ArrayList();
        int devicesTotal = reportMapper.getDeviceAllTotal();
        int devicesDay = reportMapper.getDeviceDay();
        int devicesMonth = reportMapper.getDeviceMonth();
        int devicesYear = reportMapper.getDeviceYear();
        int devicesWeek = reportMapper.getDeviceWeek();

        int usersTotal = reportMapper.getUserAllTotal();
        int usersDay = reportMapper.getUserDay();
        int usersMonth = reportMapper.getUserMonth();
        int usersYear = reportMapper.getUserYear();
        int usersWeek = reportMapper.getUserWeek();

        int exceptionsTotal = reportMapper.getExceptionAllTotal();
        int exceptionsDay = reportMapper.getExceptionDay();
        int exceptionsMonth = reportMapper.getExceptionMonth();
        int exceptionsYear = reportMapper.getExceptionYear();
        int exceptionsWeek = reportMapper.getExceptionWeek();



        Map map = new HashMap();
        map.put("name","总数");
        map.put("devices",devicesTotal);
        map.put("exceptions",exceptionsTotal);
        map.put("users",usersTotal);
        map.put("vip",1);

        Map map1 = new HashMap();
        map1.put("name","今日");
        map1.put("devices",devicesDay);
        map1.put("exceptions",exceptionsDay);
        map1.put("users",usersDay);
        map1.put("vip",0);

        Map map2 = new HashMap();
        map2.put("name","本周");
        map2.put("devices",devicesWeek);
        map2.put("exceptions",exceptionsWeek);
        map2.put("users",usersWeek);
        map2.put("vip",0);

        Map map3 = new HashMap();
        map3.put("name","本月");
        map3.put("devices",devicesMonth);
        map3.put("exceptions",exceptionsMonth);
        map3.put("users",usersMonth);
        map3.put("vip",0);

        Map map4 = new HashMap();
        map4.put("name","本年");
        map4.put("devices",devicesYear);
        map4.put("exceptions",exceptionsYear);
        map4.put("users",usersYear);
        map4.put("vip",0);


        result.add(map);
        result.add(map1);
        result.add(map2);
        result.add(map3);
        result.add(map4);


        resultMap .put("code",0);
        resultMap .put("data",result);
        resultMap .put("msg","success");
        resultMap .put("count",5);

        return JSON.toJSONString(resultMap);
    }






}
