//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.example.sys.mq;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.sys.entity.Video;
import com.example.sys.service.VideoService;
import java.util.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
public class Consumer {
    private static final Logger log = LoggerFactory.getLogger(Consumer.class);
    @Autowired
    VideoService videoService;

    public Consumer() {
    }

    @JmsListener(
            destination = "ds_vedio_web_1"
    )
    public void ListenQueue(String msg) {
        log.info("接收到queue消息：" + msg);
        JSONObject jsonObject = JSON.parseObject(msg);
        if (jsonObject.get("msgid").toString().equals("36869")) {
            log.info("接收到queue消息id：36869");
            Video video = new Video();
            video.setMsgid((Integer)jsonObject.get("msgid"));
            video.setSeq((Integer)jsonObject.get("seq"));
            video.setTerminal((String)jsonObject.get("terminal"));
            video.setChannel((Integer)jsonObject.get("channel"));
            video.setTimeinterval((Integer)jsonObject.get("timeinterval"));
            video.setSeialNo((String)jsonObject.get("seialNo"));
            video.setFileName((String)jsonObject.get("fileName"));
            String s = jsonObject.get("fileName").toString();
            video.setFile(s.substring(s.length() - 33, s.length()));
            video.setStartTime((String)jsonObject.get("startTime"));
            video.setEndTime((String)jsonObject.get("endTime"));
            video.setCreateTime(new Date());
            int b = this.videoService.insert(video);
            if (b == 1) {
                log.info("新增视频信息成功");
            } else {
                log.info("新增视频信息异常");
            }
        }

    }

    @JmsListener(
            destination = "ds_vedio_web_1",
            containerFactory = "jmsTopicListenerContainerFactory"
    )
    public void ListenTopic(String msg) {
        System.out.println("接收到topic消息：" + msg);
    }
}
