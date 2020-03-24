package com.example.logsys.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.logsys.entity.Device;
import com.example.logsys.entity.LogVo;
import com.example.logsys.netty.util.OrderType;
import com.example.logsys.netty.util.SendMessage;
import com.example.logsys.netty.util.WebSocketConstant;
import com.example.logsys.service.SendMsgService;
import io.netty.channel.ChannelId;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/message")
public class MessageController extends BaseController{

    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private SendMsgService sendMsgService;

    /**
     * 重启设备
     * */
    @PostMapping("restart")
    public String restart(@RequestBody List<LogVo> logVos){
        if(logVos.size()>0){
            HashMap<String ,Object> resultMap= new HashMap<>(16);
            resultMap.put("code", OrderType.SEND_RESTART);
            HashMap<String ,String> data= new HashMap<>(16);
            data.put("restart","1");
            resultMap.put("data",data);
            List<String> imeis = new ArrayList();
            logVos.stream().forEach(o->{
                imeis.add(o.getDevice().getImei());
            });
            SendMessage sendMessage = new SendMessage();
            sendMessage.setCode(OrderType.SEND_RESTART);
            sendMessage.setSendType(WebSocketConstant.SEND_TYPE_ONE_TO_MANY);
            sendMessage.setContent(resultMap.toString());
            sendMessage.setReceivers(imeis);
            sendMessage.setSendTime(new Date());

            String no = this.online(sendMessage.getReceivers());
            if(StringUtils.isNotEmpty(no)){
                return this.outPutData("发送失败:设备"+ no +"不在线");
            }
            sendMsgService.sendMsg(sendMessage);
        }

     return this.outPutData("发送成功");
    }

    /**
     * 上传日志文件
     * */
    @PostMapping("uploadLog")
    public String uploadLog(@RequestBody List<LogVo> logVos){
        if(logVos.size()>0){
            logVos.stream().forEach(o->{
                HashMap<String ,Object> resultMap= new HashMap<>(16);
                resultMap.put("code",OrderType.SEND_DOWNLOAD);
                HashMap<String ,String> data= new HashMap<>(16);
                data.put("download","1");
                data.put("path",o.getLogPath());
                resultMap.put("data",data);
                List<String> imeis = new ArrayList();
                imeis.add(o.getDevice().getImei());
                SendMessage sendMessage = new SendMessage();
                sendMessage.setCode(OrderType.SEND_DOWNLOAD);
                sendMessage.setSendType(WebSocketConstant.SEND_TYPE_ONE_TO_MANY);
                sendMessage.setContent(resultMap.toString());
                sendMessage.setReceivers(imeis);
                sendMessage.setSendTime(new Date());
                sendMsgService.sendMsg(sendMessage);
            });
        }
        return this.outPutData("发送成功");
    }


    /**
     * 开启/关闭MTKLOG
     * */
    @PostMapping("openLog")
    public String openLog(@RequestBody List<LogVo> logVos){
        if(logVos.size()>0){
            HashMap<String ,Object> resultMap= new HashMap<>(16);
            resultMap.put("code",OrderType.SEND_OC_MTKLOG);
            HashMap<String ,String> data= new HashMap<>(16);
            data.put("oc","1");
            resultMap.put("data",data);
            List<String> imeis = new ArrayList();
            logVos.stream().forEach(o->{
                imeis.add(o.getDevice().getImei());
            });
            SendMessage sendMessage = new SendMessage();
            sendMessage.setCode(OrderType.SEND_OC_MTKLOG);
            sendMessage.setSendType(WebSocketConstant.SEND_TYPE_ONE_TO_MANY);
            sendMessage.setContent(resultMap.toString());
            sendMessage.setReceivers(imeis);
            sendMessage.setSendTime(new Date());

            String no = this.online(sendMessage.getReceivers());
            if(StringUtils.isNotEmpty(no)){
                return this.outPutData("发送失败:设备"+ no +"不在线");
            }
            sendMsgService.sendMsg(sendMessage);
        }
        return this.outPutData("发送成功");
    }
    @PostMapping("closeLog")
    public String closeLog(@RequestBody List<LogVo> logVos){
        if(logVos.size()>0){
            HashMap<String ,Object> resultMap= new HashMap<>(16);
            resultMap.put("code",OrderType.SEND_OC_MTKLOG);
            HashMap<String ,String> data= new HashMap<>(16);
            data.put("oc","0");
            resultMap.put("data",data);
            List<String> imeis = new ArrayList();
            logVos.stream().forEach(o->{
                imeis.add(o.getDevice().getImei());
            });
            SendMessage sendMessage = new SendMessage();
            sendMessage.setCode(OrderType.SEND_OC_MTKLOG);
            sendMessage.setSendType(WebSocketConstant.SEND_TYPE_ONE_TO_MANY);
            sendMessage.setContent(resultMap.toString());
            sendMessage.setReceivers(imeis);
            sendMessage.setSendTime(new Date());

            String no = this.online(sendMessage.getReceivers());
            if(StringUtils.isNotEmpty(no)){
                return this.outPutData("发送失败:设备"+ no +"不在线");
            }
            sendMsgService.sendMsg(sendMessage);
        }
        return this.outPutData("发送成功");
    }
    /**
     * 开启/关闭抓取MTKLOG
     * */
    @PostMapping("startGrab")
    public String startGrab(@RequestBody List<LogVo> logVos){
        if(logVos.size()>0){
            HashMap<String ,Object> resultMap= new HashMap<>(16);
            resultMap.put("code",OrderType.SEND_GRAB_MTKLOG);
            HashMap<String ,String> data= new HashMap<>(16);
            data.put("grab","1");
            resultMap.put("data",data);
            List<String> imeis = new ArrayList();
            logVos.stream().forEach(o->{
                imeis.add(o.getDevice().getImei());
            });
            SendMessage sendMessage = new SendMessage();
            sendMessage.setCode(OrderType.SEND_GRAB_MTKLOG);
            sendMessage.setSendType(WebSocketConstant.SEND_TYPE_ONE_TO_MANY);
            sendMessage.setContent(resultMap.toString());
            sendMessage.setReceivers(imeis);
            sendMessage.setSendTime(new Date());

            String no = this.online(sendMessage.getReceivers());
            if(StringUtils.isNotEmpty(no)){
                return this.outPutData("发送失败:设备"+ no +"不在线");
            }
            sendMsgService.sendMsg(sendMessage);
        }
        return this.outPutData("发送成功");
    }
    @PostMapping("stopGrab")
    public String stopGrab(@RequestBody List<LogVo> logVos){
        if(logVos.size()>0){
            HashMap<String ,Object> resultMap= new HashMap<>(16);
            resultMap.put("code",OrderType.SEND_GRAB_MTKLOG);
            HashMap<String ,String> data= new HashMap<>(16);
            data.put("grab","0");
            resultMap.put("data",data);
            List<String> imeis = new ArrayList();
            logVos.stream().forEach(o->{
                imeis.add(o.getDevice().getImei());
            });
            SendMessage sendMessage = new SendMessage();
            sendMessage.setCode(OrderType.SEND_GRAB_MTKLOG);
            sendMessage.setSendType(WebSocketConstant.SEND_TYPE_ONE_TO_MANY);
            sendMessage.setContent(resultMap.toString());
            sendMessage.setReceivers(imeis);
            sendMessage.setSendTime(new Date());

            String no = this.online(sendMessage.getReceivers());
            if(StringUtils.isNotEmpty(no)){
                return this.outPutData("发送失败:设备"+ no +"不在线");
            }
            sendMsgService.sendMsg(sendMessage);
        }
        return this.outPutData("发送成功");
    }


    /**
     *  配置MTKLOGConfig
     * */
    @PostMapping("setLog")
    public String setLog(@RequestBody Map<String,Object> map){
        JSONObject json = new JSONObject(map);

        if(map.isEmpty()){
            return this.outPutErr("参数有误");
        }
        String logPath = (String) map.get("logPath");
        String logSize = (String) map.get("logSize");
        String logType = (String) map.get("logType");
        List<Object> logVos = (List<Object>) map.get("data");

        if(logVos.size()>0){
            HashMap<String ,Object> resultMap= new HashMap<>(16);
            resultMap.put("code",OrderType.SEND_SET_MTKCONFIG);
            HashMap<String ,String> data= new HashMap<>(16);
            data.put("logPath",logPath);
            data.put("logSize",logSize);
            data.put("logType",logType);
            resultMap.put("data",data);
            List<String> imeis = new ArrayList();
            logVos.stream().forEach(o->{
                JSONObject jsonObject = JSONObject.parseObject(JSONObject.toJSONString(o));
                LogVo logVo = JSON.toJavaObject(jsonObject, LogVo.class);
                imeis.add(logVo.getDevice().getImei());
            });

            SendMessage sendMessage = new SendMessage();
            sendMessage.setCode(OrderType.SEND_SET_MTKCONFIG);
            sendMessage.setSendType(WebSocketConstant.SEND_TYPE_ONE_TO_MANY);
            sendMessage.setContent(resultMap.toString());
            sendMessage.setReceivers(imeis);
            sendMessage.setSendTime(new Date());

            String no = this.online(sendMessage.getReceivers());
            if(StringUtils.isNotEmpty(no)){
                return this.outPutData("发送失败:设备"+ no +"不在线");
            }
            sendMsgService.sendMsg(sendMessage);
        }
        return this.outPutData("发送成功");
    }


    /**
     * 清除MTKLOG
     * */
    @PostMapping("clearLog")
    public String clearLog(@RequestBody List<LogVo> logVos){
        if(logVos.size()>0){
            HashMap<String ,Object> resultMap= new HashMap<>(16);
            resultMap.put("code",OrderType.SEND_CLEAR_MTKLOG);
            HashMap<String ,String> data= new HashMap<>(16);
            data.put("clear","1");
            resultMap.put("data",data);
            List<String> imeis = new ArrayList();
            logVos.stream().forEach(o->{
                imeis.add(o.getDevice().getImei());
            });
            SendMessage sendMessage = new SendMessage();
            sendMessage.setCode(OrderType.SEND_CLEAR_MTKLOG);
            sendMessage.setSendType(WebSocketConstant.SEND_TYPE_ONE_TO_MANY);
            sendMessage.setContent(resultMap.toString());
            sendMessage.setReceivers(imeis);
            sendMessage.setSendTime(new Date());

            String no = this.online(sendMessage.getReceivers());
            if(StringUtils.isNotEmpty(no)){
                return this.outPutData("发送失败:设备"+ no +"不在线");
            }
            sendMsgService.sendMsg(sendMessage);
        }
        return this.outPutData("发送成功");
    }



    /**
     * 获取终端MTK log文件列表
     * */
    @PostMapping("getLogFile")
    public String getLogFile(@RequestBody List<LogVo> logVos){
        if(logVos.size()>0){
            HashMap<String ,Object> resultMap= new HashMap<>(16);
            resultMap.put("code",OrderType.SEND_GRAB_MTKLOG);
            HashMap<String ,String> data= new HashMap<>(16);
            data.put("projectName","1");
            resultMap.put("data",data);
            List<String> imeis = new ArrayList();
            logVos.stream().forEach(o->{
                imeis.add(o.getDevice().getImei());
            });
            SendMessage sendMessage = new SendMessage();
            sendMessage.setCode(OrderType.SEND_GRAB_MTKLOG);
            sendMessage.setSendType(WebSocketConstant.SEND_TYPE_ONE_TO_MANY);
            sendMessage.setContent(resultMap.toString());
            sendMessage.setReceivers(imeis);
            sendMessage.setSendTime(new Date());

            String no = this.online(sendMessage.getReceivers());
            if(StringUtils.isNotEmpty(no)){
                return this.outPutData("发送失败:设备"+ no +"不在线");
            }
            sendMsgService.sendMsg(sendMessage);
        }
        return this.outPutData("发送成功");
    }


    /**
     * 判断设备是否在线
     * @return
     */
    public String online(List<String> receivers){
        List<String> notOnline = new ArrayList<>();
        for (String receive : receivers) {
            Set<ChannelId> members = redisTemplate.opsForSet().members(getChannelId(receive));
            if (CollectionUtils.isEmpty(members)) {
                notOnline.add(receive);
            }
        }
        if (notOnline.size()>0){
            return notOnline.stream().collect(Collectors.joining("-"));
        }
      return null;
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
