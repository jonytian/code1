package com.example.logsys.netty.util;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.logsys.entity.Device;
import com.example.logsys.entity.Log;
import com.example.logsys.entity.Message;
import com.example.logsys.entity.WebSocketPerssionVerify;
import com.example.logsys.service.DeviceService;
import com.example.logsys.service.LogService;
import com.example.logsys.service.MessageService;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelId;
import io.netty.util.AttributeKey;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class ServerHandler {

    public static ServerHandler serverHandler;

    @Autowired
    private DeviceService deviceService;
    @Autowired
    private LogService logService;
    @Autowired
    private MessageService messageService;

    @Autowired
    private RedisTemplate redisTemplate;

    //通过@PostConstruct实现初始化bean之前进行的操作
    @PostConstruct
    public void init() {
        serverHandler = this;
        serverHandler.deviceService = this.deviceService ;
        serverHandler.logService = this.logService ;
    }


    // 第一次连接保存设备信息
    public void saveUserInfo(WebSocketPerssionVerify webSocketPerssionVerify , ChannelHandlerContext ctx){

        Device device = new Device();
        device.setProjectName(webSocketPerssionVerify.getProjectName());
        device.setImei(webSocketPerssionVerify.getImei());
        device.setCode(webSocketPerssionVerify.getToken());
        device.setConnectTime(webSocketPerssionVerify.getConnectTime());
        device.setOnline(1);
        device.setKicked(0);
        device.setRestart(0);
        device.setMtklogStatus(0);
        device.setCreateTime(new Date());
        redisTemplate.opsForSet().add(WebSocketConstant.BOSS_MSG_CHANNELID + webSocketPerssionVerify.getImei(), ctx.channel().id());
        redisTemplate.expire(WebSocketConstant.BOSS_MSG_CHANNELID + webSocketPerssionVerify.getImei(), 1, TimeUnit.DAYS);
        int b = serverHandler.deviceService.insertDevices(device);
        if (b == 1) {
            log.info("*****************新增设备成功*****************");
        } else {
            log.error("*****************新增设备失败*****************");
        }

    }

    public void updateUserInfo(ChannelHandlerContext ctx){
        // 删除id
        removeChannelId(ctx);
    }
    /**
     * 删除channelId: <br>
     * @taskId <br>
     * @param ctx 上下文<br>
     */
    private void removeChannelId(ChannelHandlerContext ctx) {
        String imei = getImei(ctx);
        if (StringUtils.isNotEmpty(imei)) {
            redisTemplate.opsForSet().remove(WebSocketConstant.BOSS_MSG_CHANNELID + imei, ctx.channel().id());
            // 更新设备状态
            Device device = new Device();
            device.setOnline(0);
            device.setImei(imei);
            device.setDisconnectTime(new Date());
            int b = serverHandler.deviceService.update(device);
            if (b == 1) {
                log.info("*****************新增更新成功*****************");
            } else {
                log.error("*****************新增更新失败*****************");
            }
        }
    }
    /**
     * 获取用户id: <br>
     * @taskId <br>
     * @param ctx 上下文
     * @return <br>
     */
    private String getImei(ChannelHandlerContext ctx) {
        AttributeKey<WebSocketPerssionVerify> key = AttributeKey.valueOf("perssion");
        WebSocketPerssionVerify webSocketPerssionVerify = ctx.channel().attr(key).get();
        if (null != webSocketPerssionVerify) {
            return webSocketPerssionVerify.getImei();
        }
        return null;
    }


    // 保存异常信息
    public void saveLogInfo(String message, ChannelHandlerContext ctx)throws Exception{

        if (!StringUtils.isEmpty(message)){
            JSONObject jsonObject = JSON.parseObject(message);
            String code = (String) jsonObject.get("code");
            JSONObject data =  (JSONObject) jsonObject.get("data");
            String imei = (String) jsonObject.get("imei");
            if (!StringUtils.isEmpty(code)){
                if (!code.equals("10001")){
                    log.error("code:"+code+"-----"+"消息类型有误，仅处理code=10001类型的数据");
                    throw new Exception("仅支持JSON格式");
                }
                if (data != null ){
                    if (!StringUtils.isEmpty(imei)){
                        Device device = serverHandler.deviceService.findByImei(imei);
                        if(device !=null){
                            log.info("********************开始处理日志数据******************************");
                            Log exc = new Log();
                            exc.setException((String) data.get("exception"));
                            exc.setDescription((String) data.get("description"));
                            exc.setPackageName((String) data.get("packageName"));
                            exc.setHappenTime((String) data.get("happenTime"));
                            exc.setExpirationTime(new Date());
                            exc.setLogPath((String) data.get("logPath"));
                            exc.setRomVersion((String) data.get("romVersion"));
                            exc.setAndroidVersion((String) data.get("romVersion"));
                            exc.setDeviceId(device.getDeviceId());
                            exc.setCreateTime(new Date());
                            int b = serverHandler.logService.insertLogs(exc);
                            if (b == 1) {
                                log.info("*****************新增日志成功*****************");
                            } else {
                                log.error("*****************新增日志失败*****************");
                            }
                            // 保存消息
                            saveMsgRecord(message,ctx);
                        }
                    }
                }
            }
        }
    }

    /**
     * 保存消息记录
     * @param msg
     */
    private void saveMsgRecord(String msg, ChannelHandlerContext ctx) {
        log.info("*****************正在保存消息*****************");
        JSONObject jsonObject = JSON.parseObject(msg);
        String code = (String) jsonObject.get("code");
        JSONObject data = (JSONObject) jsonObject.get("data");
        String imei = (String) jsonObject.get("imei");

        Message message = new Message();
        message.setImei(imei);
        message.setSender(imei);
        message.setReceiver("admin");
        message.setChannelId(ctx.channel().id().toString());
        message.setSendTime(new Date());
        message.setCode(code);
        message.setContent(data.toJSONString());
        message.setSendTime(new Date());
        messageService.saveMessage(message);
        log.info("*****************保存消息成功*****************");
    }




}
