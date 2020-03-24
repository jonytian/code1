package com.pjb.springbootjjwt.api;

import com.alibaba.fastjson.JSONObject;
import com.pjb.springbootjjwt.annotation.UserLoginToken;
import com.pjb.springbootjjwt.entity.Device;
import com.pjb.springbootjjwt.entity.User;
import com.pjb.springbootjjwt.service.DeviceService;
import com.pjb.springbootjjwt.service.TokenService;
import com.pjb.springbootjjwt.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author jinbin
 * @date 2018-07-08 20:45
 */
@RestController
@RequestMapping("api")
public class DeviceApi {
    @Autowired
    DeviceService deviceService;
    @Autowired
    TokenService tokenService;
    //登录
    @PostMapping("/auth")
    public Object login(@RequestBody Device device){
        JSONObject jsonObject=new JSONObject();
        Device deviceForBase=deviceService.findByImei(device);
        if(deviceForBase==null){
            jsonObject.put("data","认证失败,设备不存在");
            return jsonObject;
        }else {
            if (!deviceForBase.getProjectName().equals(device.getProjectName())){
                jsonObject.put("data","认证失败,信息错误");
                return jsonObject;
            }else {
                String token = tokenService.getDeviceToken(deviceForBase);
                jsonObject.put("token", token);
                jsonObject.put("device", deviceForBase);
                return jsonObject;
            }
        }
    }
    @UserLoginToken
    @GetMapping("/getContent")
    public String getMessage(){
        return "你已通过验证";
    }
}
