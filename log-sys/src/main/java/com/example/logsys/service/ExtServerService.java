package com.example.logsys.service;

import com.example.logsys.entity.Device;
import com.example.logsys.entity.MsgAgreement;
import com.example.logsys.entity.UserChannelInfo;
import com.example.logsys.redis.Publisher;
import com.example.logsys.redis.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

@Slf4j
@Service("extServerService")
public class ExtServerService {

    @Resource
    private Publisher publisher;
    @Resource
    private RedisUtil redisUtil;

    @Autowired
    private DeviceService deviceService;

    public void push(MsgAgreement msgAgreement){
        publisher.pushMessage("itstack-demo-netty-push-msgAgreement", msgAgreement);
    }

    public RedisUtil getRedisUtil() {
        return redisUtil;
    }

    public void saveUserInfo (UserChannelInfo userChannelInfo){
        Device device = new Device();
        device.setImei(userChannelInfo.getChannelId());
        device.setCode(userChannelInfo.getChannelId());
        device.setProjectName(userChannelInfo.getChannelId());
        device.setConnectTime(new Date());
        device.setCreateTime(new Date());
        int b = deviceService.insertDevices(device);
        if (b == 1){
            log.info("**************新增设备成功！*********************");
        }else {
            log.info("**************新增设备失败！*********************");
        }
    }

}
