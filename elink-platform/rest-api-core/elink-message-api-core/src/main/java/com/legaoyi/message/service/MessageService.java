package com.legaoyi.message.service;

import java.util.List;
import java.util.Map;

import org.apache.commons.net.ftp.FTPClient;

import com.legaoyi.gateway.message.model.DeviceDownMessage;
import com.legaoyi.gateway.message.model.DeviceHistoryMedia;

/**
 * @author gaoshengbo
 */
public interface MessageService {

    /**
     * 808下行消视频监控息发送接口
     *
     * @param enterpriseId
     * @param deviceId
     * @param clinetId
     * @param messageBody
     * @param gatewayId
     * @return DeviceDownMessage
     * @throws Exception
     */
    public DeviceDownMessage start808Video(String enterpriseId, String deviceId, String clinetId, Map<String, Object> messageBody, String gatewayId) throws Exception;

    /**
     * 808下行消视频监控息发送接口
     *
     * @param enterpriseId
     * @param deviceId
     * @param channelId
     * @param clinetId
     * @param gatewayId
     * @throws Exception
     */
    public void stop808Video(String enterpriseId, String deviceId, String channelId, String clinetId, String gatewayId) throws Exception;

    /**
     * 1078下行消视频监控息发送接口
     *
     * @param enterpriseId
     * @param deviceId
     * @param clinetId
     * @param messageBody
     * @param gatewayId
     * @return DeviceDownMessage
     * @throws Exception
     */
    public DeviceDownMessage start1078Video(String enterpriseId, String deviceId, String clinetId, Map<String, Object> messageBody, String gatewayId) throws Exception;
    
    
    /**
     * 1078语音对讲发送接口
     *
     * @param enterpriseId
     * @param deviceId
     * @param clinetId
     * @param messageBody
     * @param gatewayId
     * @return DeviceDownMessage
     * @throws Exception
     */
    public DeviceDownMessage start1078Talk(String enterpriseId, String deviceId, String clinetId, Map<String, Object> messageBody, String gatewayId) throws Exception;

    /**
     * 1078下行消视频监控息发送接口
     *
     * @param enterpriseId
     * @param deviceId
     * @param channelId
     * @param clinetId
     * @param gatewayId
     * @throws Exception
     */
    public void stop1078Video(String enterpriseId, String deviceId, String clinetId, Map<String, Object> messageBody, String gatewayId) throws Exception;

    /**
     * 视频监控心跳
     *
     * @param clinetId
     * @param type
     * @throws Exception
     */
    public void setHeartbeat(String clinetId, String messageId) throws Exception;

    /**
     * 808下行消息发送接口
     *
     * @param enterpriseId
     * @param deviceId
     * @param messageId
     * @param messageBody
     * @param gatewayId
     * @return DeviceDownMessage
     * @throws Exception
     */
    public DeviceDownMessage send(String enterpriseId, String deviceId, String messageId, Map<String, Object> messageBody, String gatewayId) throws Exception;

    /**
     * 808围栏消息发送接口
     *
     * @param enterpriseId
     * @param deviceId
     * @param messageId
     * @param messageBody
     * @param gatewayId
     * @return DeviceDownMessage
     * @throws Exception
     */
    public DeviceDownMessage setFence(String enterpriseId, String deviceId, String messageId, Map<String, Object> messageBody, String gatewayId) throws Exception;

    /***
     * 
     * @param enterpriseId
     * @param deviceId
     * @param messageId
     * @param ids
     * @param gatewayId
     * @return
     * @throws Exception
     */
    public DeviceDownMessage delFence(String enterpriseId, String deviceId, String messageId, String[] ids, String gatewayId) throws Exception;

    /**
     * 808批量下行消息发送接口，返回错误的设备id
     *
     * @param enterpriseId
     * @param messageId
     * @param messageBody
     * @return List
     * @throws Exception
     */
    public List<?> batchSend(String enterpriseId, String messageId, Map<String, Object> messageBody) throws Exception;

    /**
     * 根据消息id列表获取消息状态
     *
     * @param enterpriseId
     * @param ids
     * @return List
     * @throws Exception
     */
    public List<?> getDeviceDownMessageState(String enterpriseId, String ids[]) throws Exception;

    /**
     * 强制下线终端
     *
     * @param simCode
     * @param gatewayId
     * @throws Exception
     */
    public void forceOffline(String simCode, String gatewayId) throws Exception;

    /**
     * 解除黑名单
     *
     * @param id
     * @throws Exception
     */
    public void removeBlacklist(String id) throws Exception;

    /**
     * 获取终端上传的文件列表
     * 
     * @param id
     * @return
     * @throws Exception
     */
    public List<?> getUploadFileList(String id) throws Exception;

    /**
     * 上传文件消息
     * 
     * @param enterpriseId
     * @param deviceId
     * @param messageId
     * @param messageBody
     * @param gatewayId
     * @throws Exception
     */
    public DeviceHistoryMedia uploadFile(String enterpriseId, String deviceId, String messageId, Map<String, Object> messageBody, String gatewayId) throws Exception;

    /**
     * 上传文件控制消息
     * 
     * @param enterpriseId
     * @param deviceId
     * @param messageId
     * @param messageBody
     * @param gatewayId
     * @throws Exception
     */
    public DeviceDownMessage uploadControl(String enterpriseId, String deviceId, String messageId, Map<String, Object> messageBody, String gatewayId) throws Exception;

    /**
     * 历史音视频资源查询指令
     * 
     * @param enterpriseId
     * @param deviceId
     * @param messageId
     * @param messageBody
     * @param gatewayId
     * @throws Exception
     */
    public DeviceDownMessage queryFileResource(String enterpriseId, String deviceId, String messageId, Map<String, Object> messageBody, String gatewayId) throws Exception;

    /**
     * 
     * @return
     * @throws Exception
     */
    public FTPClient getFtpClient() throws Exception;

}
