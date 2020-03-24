package com.example.logsys.redis;

import com.alibaba.fastjson.JSON;
import com.example.logsys.entity.UserChannelInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service("redisUtil")
public class RedisUtil {

    @Autowired
    private StringRedisTemplate redisTemplate;

    public void pushObj(UserChannelInfo userChannelInfo) {
        redisTemplate.opsForHash().put("itstack-demo-netty-2-09-user", userChannelInfo.getChannelId(), JSON.toJSONString(userChannelInfo));
    }

    public List<UserChannelInfo> popList() {
        List<Object> values = redisTemplate.opsForHash().values("itstack-demo-netty-2-09-user");
        if (null == values) return new ArrayList<>();
        List<UserChannelInfo> userChannelInfoList = new ArrayList<>();
        for (Object strJson : values) {
            userChannelInfoList.add(JSON.parseObject(strJson.toString(), UserChannelInfo.class));
        }
        return userChannelInfoList;
    }

    public void remove(String channelId) {
        redisTemplate.opsForHash().delete("itstack-demo-netty-2-09-user",channelId);
    }

    public void clear(){
        redisTemplate.delete("itstack-demo-netty-2-09-user");
    }

}
