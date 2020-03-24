package com.legaoyi.client.message.sender;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.legaoyi.client.SpringBeanUtil;
import com.legaoyi.client.message.Message;
import com.legaoyi.client.message.builder.MessageBuilder;
import com.legaoyi.client.up.messagebody.JTT808_0200_2011_MessageBody;
import com.legaoyi.client.up.messagebody.JTT808_0F10_obd_MessageBody;
import com.legaoyi.protocol.message.MessageBody;

import io.netty.channel.ChannelHandlerContext;

@Component("gpsMessageSender")
public class GpsMessageSender extends MessageSender {

    private static final Logger logger = LoggerFactory.getLogger(GpsMessageSender.class);

    @Value("${elink.device.protocol.version}")
    private String protocolVersion;

    @Value("${elink.device.gps.idleTime}")
    private int idleTime = 30;

    private static List<Map<String, Object>> gpsInfoList = new ArrayList<Map<String, Object>>();

    private volatile boolean isRunning = false;

    @PostConstruct
    public void init() {
        String gpsFile = GpsMessageSender.class.getClass().getResource("/").getPath() + "gps.txt";
        try {
            FileReader fr = new FileReader(gpsFile);
            BufferedReader bf = new BufferedReader(fr);
            Map<String, Object> map;
            String str;
            // 按行读取字符串
            while ((str = bf.readLine()) != null) {
                String[] arr = str.split(",");
                map = new HashMap<String, Object>();
                map.put("lng", Double.valueOf(arr[0]));
                map.put("lat", Double.valueOf(arr[1]));
                if (arr.length > 2) {
                    map.put("speed", Double.valueOf(arr[2]));
                }
                if (arr.length > 3) {
                    map.put("direction", Integer.valueOf(arr[3]));
                }
                if (arr.length > 4) {
                    map.put("altitude", Integer.valueOf(arr[4]));
                }
                gpsInfoList.add(map);
            }
            bf.close();
            fr.close();
        } catch (IOException e) {
            logger.error("", e);
        }
    }

    public void send(ChannelHandlerContext ctx, Message message) throws Exception {
        if (isRunning) {
            return;
        }
        isRunning = true;
        MessageBuilder messageBuilder = SpringBeanUtil.getMessageBuilder(JTT808_0200_2011_MessageBody.JTT808_MESSAGE_ID, protocolVersion);

        MessageBuilder obdCodeBuilder = SpringBeanUtil.getMessageBuilder(JTT808_0F10_obd_MessageBody.JTT808_MESSAGE_ID, "obd");
        while (true) {
            for (int i = 0, l = gpsInfoList.size(); i < l; i++) {
                Map<String, Object> map = gpsInfoList.get(i);
                MessageBody messageBody = messageBuilder.build(map);
                message.setMessageBody(messageBody);
                message.getMessageHeader().setMessageId(JTT808_0200_2011_MessageBody.JTT808_MESSAGE_ID);
                super.send(ctx, message);
                logger.info("******up message,message={}", message.toString());

                if (protocolVersion.endsWith("obd")) {
                    messageBody = obdCodeBuilder.build(map);
                    if (messageBody != null) {
                        message.setMessageBody(messageBody);
                        message.getMessageHeader().setMessageId(JTT808_0F10_obd_MessageBody.JTT808_MESSAGE_ID);
                        super.send(ctx, message);
                        logger.info("******up message,message={}", message.toString());
                    }
                }

                Thread.sleep(idleTime * 1000);
            }
        }
    }

}
