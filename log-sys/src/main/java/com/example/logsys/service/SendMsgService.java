package com.example.logsys.service;

import com.example.logsys.entity.Device;
import com.example.logsys.entity.Message;
import com.example.logsys.entity.User;
import com.example.logsys.entity.WebSocketPerssionVerify;
import com.example.logsys.netty.MyChannelHandler;
import com.example.logsys.netty.util.SendMessage;
import com.example.logsys.netty.util.WebSocketConstant;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelId;
import io.netty.channel.group.ChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.AttributeKey;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.*;
@Slf4j
@Service
public class SendMsgService {

    @SuppressWarnings("rawtypes")
    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private DeviceService deviceService;

    @Autowired
    private MessageService messageService;

    /**
     * 发送消息
     *  @param sendMessage 消息体
     */
    public void sendMsg(SendMessage sendMessage) {

        if (WebSocketConstant.SEND_TYPE_ONE_TO_ONE.equals(sendMessage.getSendType())
                || WebSocketConstant.SEND_TYPE_ONE_TO_MANY.equals(sendMessage.getSendType())) {
            send(sendMessage);
        } else if (WebSocketConstant.SEND_TYPE_GROUP.equals(sendMessage.getSendType())) {
            sendToAll(sendMessage);
        }

    }

    /**
     * 发送一对多消息
     * @param sendMessage
     */
    @SuppressWarnings("unchecked")
    private void    send(SendMessage sendMessage) {
        ChannelGroup channelGroup = MyChannelHandler.getChannelGroup();
        if (null != channelGroup) {
            List<String> receivers = sendMessage.getReceivers();
            if (CollectionUtils.isNotEmpty(receivers)) {
                for (String receive : receivers) {
                    Set<ChannelId> members = redisTemplate.opsForSet().members(getChannelId(receive));
                    if (CollectionUtils.isNotEmpty(members)) {
                        for (ChannelId channelId : members) {
                            if (null != channelId) {
                                sendToOne(channelId, channelGroup, sendMessage,receive);
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * 一对多发送消息
     * @param channelId 渠道id
     * @param channelGroup channel组
     * @param sendMessage 消息
     */
    private void sendToOne(ChannelId channelId, ChannelGroup channelGroup, SendMessage sendMessage ,String receive) {
        if (null != channelId) {
            Channel channel = channelGroup.find(channelId);
            if (null != channel) {
                ChannelFuture channelFuture = channel.writeAndFlush(new TextWebSocketFrame(sendMessage.getContent()));
//                // 获取用户信息
//                AttributeKey<WebSocketPerssionVerify> key = AttributeKey.valueOf("perssion");
//                WebSocketPerssionVerify webSocketPerssionVerify = channel.attr(key).get();
//                String imei = webSocketPerssionVerify.getImei();
//                sendMessage.setReceivers(Arrays.asList(imei));
//                sendMessage.setSendTime(new Date());



                // 消息发送成功后，保存发送记录
                channelFuture.addListener(future -> {
                    if (future.isSuccess()) {
                        Message message = new Message();
                        message.setImei(receive);
                        message.setSender("admin");
                        message.setReceiver(receive);
                        message.setCode(sendMessage.getCode());
                        message.setChannelId(channelId.toString());
                        message.setContent(sendMessage.getContent());
                        message.setSendTime(new Date());
                        messageService.saveMessage(message);
                    }
                });
            }
        }
    }

    /**
     * 群发消息
     * @param sendMessage
     */
    private void sendToAll(SendMessage sendMessage) {
        ChannelGroup channelGroup = MyChannelHandler.getChannelGroup();
        if (null != channelGroup) {
            channelGroup.writeAndFlush(new TextWebSocketFrame(sendMessage.getContent()));
            saveMsgRecord(sendMessage);
        }
    }



    /**
     * 保存消息记录
     * @param sendMessage
     */
    private void saveMsgRecord(SendMessage sendMessage) {

        List<String> receivers = sendMessage.getReceivers();
        if (CollectionUtils.isNotEmpty(receivers)) {
            receivers.stream().forEach(o->{
                Set<ChannelId> members = redisTemplate.opsForSet().members(getChannelId(o));
                if (CollectionUtils.isNotEmpty(members)) {
                    for (ChannelId channelId : members) {
                        if (null != channelId) {
                            Message message = new Message();
                            message.setImei(o);
                            message.setSender("admin");
                            message.setReceiver(o);
                            message.setChannelId(channelId.toString());
                            message.setSendTime(sendMessage.getSendTime());
                            message.setCode(sendMessage.getCode());
                            message.setContent(sendMessage.getContent());
                            message.setSendTime(new Date());
                            messageService.saveMessage(message);
                        }
                    }
                }

            });
        }
    }



        /**
         * 获取channelId
         * @return
         */
        private String getChannelId(String imei) {
            StringBuilder sb = new StringBuilder();
            sb.append(WebSocketConstant.BOSS_MSG_CHANNELID).append(imei);
            return sb.toString();
        }






}
