package com.example.logs.service;


import com.example.logs.entity.Device;
import com.example.logs.entity.MsgAgreement;
import com.example.logs.entity.UserChannelInfo;
import com.example.logs.redis.Publisher;
import com.example.logs.redis.RedisUtil;
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


}
