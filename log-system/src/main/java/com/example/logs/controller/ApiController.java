package com.example.logs.controller;

import com.alibaba.fastjson.JSONObject;
import com.example.logs.entity.Device;
import com.example.logs.service.DeviceService;
import com.example.logs.service.TokenService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;


@Slf4j
@RestController
@RequestMapping("api")
public class ApiController extends BaseController {

        @Autowired
        DeviceService deviceService;
        @Autowired
        TokenService tokenService;

        // 认证+获得token
        @PostMapping("/auth")
        public Object auth(@RequestParam(value = "imei", required = false) String imei,
                           @RequestParam(value = "projectName", required = false) String projectName){
            JSONObject jsonObject=new JSONObject();
            if(StringUtils.isEmpty(imei)){
                jsonObject.put("msg","认证失败,参数有误");
                return jsonObject;
            }
            if(imei.length() !=15 ){
                jsonObject.put("msg","认证失败,参数有误");
                return jsonObject;
            } if(!isLetterDigit(imei)){
                jsonObject.put("msg","认证失败,参数有误");
                return jsonObject;
            }

            if(StringUtils.isEmpty(projectName)){
                jsonObject.put("msg","认证失败,参数有误");
                return jsonObject;
            }

            Device device=deviceService.findByImei(imei);

            if(device==null){
                Device device_new = new Device();
                device_new.setProjectName(projectName);
                device_new.setImei(imei);
                String token = tokenService.getToken(device_new);
                device_new.setToken(token);
                device_new.setOnline(0);
                device_new.setKicked(0);
                device_new.setRestart(0);
                device_new.setMtklogStatus(0);
                device_new.setCreateTime(new Date());
                int b = deviceService.insertDevices(device_new);
                if (b == 1) {
                    log.info("*****************新增设备成功*****************");
                } else {
                    log.error("*****************新增设备失败*****************");
                }
                jsonObject.put("token", token);
//                jsonObject.put("device", device_new);
                return jsonObject;
            }else {
                if (!device.getProjectName().equals(projectName)){
                    jsonObject.put("msg","认证失败,设备信息有误");
                    return jsonObject;
                }
                String token = tokenService.getToken(device);
                device.setToken(token);
                int b = deviceService.update(device);
                if (b == 1) {
                    log.info("*****************更新设备成功*****************");
                } else {
                    log.error("*****************更新设备失败*****************");
                }
                jsonObject.put("token", token);
//                jsonObject.put("device", device);
                return jsonObject;
            }


        }

    }
