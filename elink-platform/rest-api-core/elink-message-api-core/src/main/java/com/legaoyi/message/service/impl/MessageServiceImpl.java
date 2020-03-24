package com.legaoyi.message.service.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.Cache.ValueWrapper;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Maps;
import com.legaoyi.common.exception.BizProcessException;
import com.legaoyi.common.message.ExchangeMessage;
import com.legaoyi.common.util.Constants;
import com.legaoyi.common.util.DateUtils;
import com.legaoyi.common.util.JsonUtil;
import com.legaoyi.common.util.Reflect2Entity;
import com.legaoyi.gateway.message.model.DeviceDownMessage;
import com.legaoyi.gateway.message.model.DeviceHistoryMedia;
import com.legaoyi.management.model.DeviceAlarmSetting;
import com.legaoyi.management.model.DeviceDataLimitAlarm;
import com.legaoyi.management.model.Dictionary;
import com.legaoyi.message.service.MessageService;
import com.legaoyi.mq.MQMessageProducer;
import com.legaoyi.persistence.jpa.service.GeneralService;
import com.legaoyi.persistence.redis.service.RedisService;
import com.legaoyi.platform.ext.service.ExtendService;
import com.legaoyi.platform.model.Device;
import com.legaoyi.platform.model.EnterpriseConfig;
import com.legaoyi.platform.service.DeviceService;

@Service("messageService")
@Transactional
public class MessageServiceImpl implements MessageService {

    private static final String MESSAGEID_JTT808_VIDEO = "8801";

    private static final String MESSAGEID_JTT1078_START_VIDEO = "9101";

    private static final String MESSAGEID_JTT1078_STOP_VIDEO = "9102";

    private static final String MESSAGEID_JTT1078_START_HISTORY_VIDEO = "9201";

    @Autowired
    @Qualifier("generalService")
    private GeneralService service;

    @Autowired
    @Qualifier("commonDownMessageProducer")
    private MQMessageProducer commonDownMessageProducer;

    @Autowired
    @Qualifier("videoMessageDownProducer")
    private MQMessageProducer videoMessageDownProducer;

    @Autowired
    @Qualifier("redisService")
    private RedisService<?> redisService;

    @Autowired
    @Qualifier("cacheManager")
    private CacheManager cacheManager;

    @Autowired
    @Qualifier("deviceExtendService")
    private ExtendService deviceExtendService;

    @Autowired
    @Qualifier("deviceService")
    private DeviceService deviceService;

    private static final long MAX_MESSAGE_SEQ = 65535;

    @Override
    public DeviceDownMessage start808Video(String enterpriseId, String deviceId, String clinetId, Map<String, Object> messageBody, String gatewayId) throws Exception {
        synchronized (deviceId) {
            Device device = (Device) this.deviceExtendService.get(deviceId);
            String messageId = String.valueOf(messageBody.get("messageId"));
            String channelId = String.valueOf(messageBody.get("channelId"));
            DeviceDownMessage message = isPlaying(device.getSimCode(), messageId, channelId, clinetId);
            if (message != null) {
                return message;
            }
            message = this.send(enterpriseId, deviceId, messageId, messageBody, gatewayId);
            this.setVideoCache(message, device.getSimCode(), channelId, clinetId, null);
            return message;
        }
    }

    @Override
    public void stop808Video(String enterpriseId, String deviceId, String channelId, String clinetId, String gatewayId) throws Exception {
        synchronized (deviceId) {
            Device device = (Device) this.deviceExtendService.get(deviceId);
            String messageId = MESSAGEID_JTT808_VIDEO;
            String cacheKey = device.getSimCode().concat("_").concat(messageId).concat("_").concat(channelId);
            if (this.redisService.sRem(Constants.CACHE_NAME_DEVICE_VIDEO_CLIENT_CACHE.concat(cacheKey), clinetId)) {
                // 移除心跳缓存
                // this.cacheManager.getCache(Constants.CACHE_NAME_DEVICE_VIDEO_CLIENT_HEARTBEAT_CACHE.concat(messageId)).evict(clinetId);
                // 设备视频缓存
                this.redisService.sRem(Constants.CACHE_NAME_DEVICE_VIDEO_LIST_CACHE, cacheKey);
                this.cacheManager.getCache(Constants.CACHE_NAME_DEVICE_VIDEO_INFO_CACHE).evict(cacheKey);
                List<String> list = this.redisService.sMembers(Constants.CACHE_NAME_DEVICE_VIDEO_CLIENT_CACHE.concat(cacheKey));
                if (list == null || list.isEmpty()) {
                    Map<String, Object> messageBody = new HashMap<String, Object>();
                    messageBody.put("deviceId", deviceId);
                    messageBody.put("channelId", channelId);
                    messageBody.put("commandType", 0);
                    messageBody.put("messageId", messageId);
                    this.send(enterpriseId, deviceId, messageId, messageBody, gatewayId);
                }
            }
        }
    }

    @Override
    public DeviceDownMessage start1078Video(String enterpriseId, String deviceId, String clinetId, Map<String, Object> messageBody, String gatewayId) throws Exception {
        Device device = (Device) this.deviceExtendService.get(deviceId);
        String messageId = String.valueOf(messageBody.get("messageId"));
        String channelId = String.valueOf(messageBody.get("channelId"));
        DeviceDownMessage message = isPlaying(device.getSimCode(), messageId, channelId, clinetId);
        if (message != null) {
            return message;
        }
        setLiveVideoServer(messageBody, null);
        String serverIp = (String) messageBody.get("ip");
        message = this.send(enterpriseId, deviceId, messageId, messageBody, gatewayId);
        this.setVideoCache(message, device.getSimCode(), channelId, clinetId, serverIp);
        return message;
    }

    @Override
    public DeviceDownMessage start1078Talk(String enterpriseId, String deviceId, String clinetId, Map<String, Object> messageBody, String gatewayId) throws Exception {
        Device device = (Device) this.deviceExtendService.get(deviceId);
        String messageId = String.valueOf(messageBody.get("messageId"));
        String channelId = String.valueOf(messageBody.get("channelId"));
        if (isTalking(device.getSimCode())) {
            throw new BizProcessException("当前车辆语音对讲已开启，语音对讲只支持一对一通话！");
        }

        setLiveVideoServer(messageBody, "3");// 语音对讲服务器
        String serverIp = (String) messageBody.get("ip");
        DeviceDownMessage message = this.send(enterpriseId, deviceId, messageId, messageBody, gatewayId);
        this.setVideoCache(message, device.getSimCode(), channelId, clinetId, serverIp);

        this.cacheManager.getCache(Constants.CACHE_NAME_DEVICE_VIDEO_INFO_CACHE).put("_talking_".concat(device.getSimCode()), clinetId);
        return message;
    }

    @Override
    public void stop1078Video(String enterpriseId, String deviceId, String clinetId, Map<String, Object> messageBody, String gatewayId) throws Exception {
        synchronized (deviceId) {
            Device device = (Device) this.deviceExtendService.get(deviceId);
            String messageId = String.valueOf(messageBody.get("messageId"));
            String channelId = String.valueOf(messageBody.get("channelId"));

            String startMessageId = messageId.equals(MESSAGEID_JTT1078_STOP_VIDEO) ? MESSAGEID_JTT1078_START_VIDEO : MESSAGEID_JTT1078_START_HISTORY_VIDEO;
            String cacheKey = device.getSimCode().concat("_").concat(startMessageId).concat("_").concat(channelId);
            if (this.redisService.sRem(Constants.CACHE_NAME_DEVICE_VIDEO_CLIENT_CACHE.concat(cacheKey), clinetId)) {
                // 移除心跳缓存
                // this.cacheManager.getCache(Constants.CACHE_NAME_DEVICE_VIDEO_CLIENT_HEARTBEAT_CACHE.concat(startMessageId)).evict(clinetId);

                this.redisService.sRem(Constants.CACHE_NAME_DEVICE_VIDEO_LIST_CACHE, cacheKey);
                ValueWrapper cache = this.cacheManager.getCache(Constants.CACHE_NAME_DEVICE_VIDEO_INFO_CACHE).get(cacheKey);

                String ip = null;
                if (cache != null && cache.get() != null) {
                    Map<?, ?> data = (Map<?, ?>) cache.get();
                    if (data != null) {
                        ip = (String) data.get("ip");
                    }
                }

                this.cacheManager.getCache(Constants.CACHE_NAME_DEVICE_VIDEO_INFO_CACHE).evict(cacheKey);
                List<String> list = this.redisService.sMembers(Constants.CACHE_NAME_DEVICE_VIDEO_CLIENT_CACHE.concat(cacheKey));
                if (list == null || list.isEmpty()) {
                    // 下发指令
                    if (StringUtils.isNotBlank(ip)) {
                        this.redisService.decr(Constants.CACHE_NAME_VIDEO_GATEWAY_CLIENT_COUNT_CACHE_PREFIX.concat(ip));
                    }
                    messageBody.put("deviceId", deviceId);
                    setVideoWhiteList(device.getSimCode(), 0);
                    this.send(enterpriseId, deviceId, messageId, messageBody, gatewayId);
                }
            }
            // 删除语音对讲缓存
            this.cacheManager.getCache(Constants.CACHE_NAME_DEVICE_VIDEO_INFO_CACHE).evict("_talking_".concat(device.getSimCode()));
        }
    }

    @Override
    public void setHeartbeat(String clinetId, String messageId) throws Exception {
        ValueWrapper cache = this.cacheManager.getCache(Constants.CACHE_NAME_DEVICE_VIDEO_CLIENT_HEARTBEAT_CACHE.concat(messageId)).get(clinetId);
        if (cache != null && cache.get() != null) {
            this.cacheManager.getCache(Constants.CACHE_NAME_DEVICE_VIDEO_CLIENT_HEARTBEAT_CACHE.concat(messageId)).put(clinetId, System.currentTimeMillis());
            redisService.expire(Constants.CACHE_NAME_DEVICE_VIDEO_CLIENT_HEARTBEAT_CACHE.concat(messageId).concat("::").concat(clinetId), 60);
        }
    }

    @Override
    public DeviceDownMessage send(String enterpriseId, String deviceId, String messageId, Map<String, Object> messageBody, String gatewayId) throws Exception {
        EnterpriseConfig config = (EnterpriseConfig) this.service.get(EnterpriseConfig.ENTITY_NAME, enterpriseId);
        if (config != null) {
            String downMessageLimit = config.getDownstreamMessageLimit();
            if (!StringUtils.isBlank(downMessageLimit) && downMessageLimit.indexOf(messageId) == -1) {
                throw new BizProcessException("指令不合法或者无操作权限");
            }
        }

        Device device = (Device) this.deviceExtendService.get(deviceId);
        DeviceDownMessage message = saveDeviceDownMessage(device, messageId, messageBody, null);

        String exchangeId = message.getId();
        Map<String, Object> messageHeader = new HashMap<String, Object>();
        messageHeader.put("simCode", device.getSimCode());
        messageHeader.put("messageId", messageId);
        messageHeader.put("messageSeq", message.getMessageSeq());

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("messageHeader", messageHeader);
        map.put("messageBody", messageBody);

        // 下行消息队列的routingKey的命名规则：gatewayId
        String routingKey = gatewayId;
        this.commonDownMessageProducer.send(routingKey, new ExchangeMessage(ExchangeMessage.MESSAGEID_PLATFORM_DOWN_MESSAGE, map, exchangeId, gatewayId));
        return message;
    }

    @Override
    public DeviceDownMessage setFence(String enterpriseId, String deviceId, String messageId, Map<String, Object> messageBody, String gatewayId) throws Exception {
        DeviceDownMessage message = this.send(enterpriseId, deviceId, messageId, messageBody, gatewayId);
        List<?> areaList = (List<?>) messageBody.get("areaList");
        if (areaList != null) {
            for (Object o : areaList) {
                Map<?, ?> map = (Map<?, ?>) o;
                saveAlarmSetting(enterpriseId, deviceId, messageId, map);
            }
        } else {
            saveAlarmSetting(enterpriseId, deviceId, messageId, messageBody);
        }
        return message;
    }

    private void saveAlarmSetting(String enterpriseId, String deviceId, String messageId, Map<?, ?> map) throws Exception {
        DeviceAlarmSetting setting = new DeviceAlarmSetting();
        setting.setBizType((short) 2);// 终端围栏
        setting.setType((short) 2);// 终端围栏告警
        setting.setDeviceId(deviceId);
        setting.setEnterpriseId(enterpriseId);
        String name = (String) map.get("name");
        setting.setName(name == null ? "-" : name);
        setting.setBizId(messageId);
        String startTime = (String) map.get("startTime");
        if (StringUtils.isNotBlank(startTime)) {
            setting.setStartTime(DateUtils.parse(startTime.replace("0000-00-00", "1970-01-01"), DateUtils.DATETIME_FORMAT));
        }
        String endTime = (String) map.get("endTime");
        if (StringUtils.isNotBlank(endTime)) {
            setting.setEndTime(DateUtils.parse(endTime.replace("0000-00-00", "1970-01-01"), DateUtils.DATETIME_FORMAT));
        }
        setting.setSetting(JsonUtil.covertObjectToString(map));
        this.service.persist(setting);
    }

    @SuppressWarnings("unchecked")
    @Override
    public DeviceDownMessage delFence(String enterpriseId, String deviceId, String messageId, String[] ids, String gatewayId) throws Exception {
        Map<String, Object> messageBody = new HashMap<String, Object>();
        List<Integer> areaIds = new ArrayList<Integer>();
        for (String id : ids) {
            if ("-1".equals(id)) {
                Map<String, Object> andCondition = new HashMap<String, Object>();
                andCondition.put("deviceId", deviceId);
                andCondition.put("bizId", String.valueOf((Integer.parseInt(messageId) - 1)));
                andCondition.put("bizType", 2);
                andCondition.put("type", 2);
                this.service.delete(DeviceAlarmSetting.ENTITY_NAME, andCondition);
            } else {
                DeviceAlarmSetting alarmSetting = (DeviceAlarmSetting) service.get(DeviceAlarmSetting.ENTITY_NAME, id);
                String setting = alarmSetting.getSetting();
                Map<String, Object> map = JsonUtil.convertStringToObject(setting, Map.class);
                Integer areaId = (Integer) map.get("areaId");
                areaIds.add(areaId);
                this.service.delete(alarmSetting);
            }
        }
        messageBody.put("areaIds", areaIds);
        DeviceDownMessage message = this.send(enterpriseId, deviceId, messageId, messageBody, gatewayId);
        return message;
    }

    @Override
    public List<?> batchSend(String enterpriseId, String messageId, Map<String, Object> messageBody) throws Exception {
        String deviceIds = (String) messageBody.get("deviceIds");
        if (StringUtils.isBlank(deviceIds)) {
            throw new BizProcessException("非法参数");
        }
        messageBody.remove("deviceIds");
        List<String> errorList = new ArrayList<String>();
        String[] arr = deviceIds.split(",");
        for (String deviceId : arr) {
            Device device = (Device) this.deviceExtendService.get(deviceId);
            if (device != null && device.getEnterpriseId().startsWith(enterpriseId)) {
                String gatewayId = this.deviceService.getGateway(deviceId);
                if (StringUtils.isBlank(gatewayId) || this.deviceService.getStatus(deviceId, gatewayId) != 3) {
                    // 缓存消息
                    saveDeviceDownMessage(device, messageId, messageBody, (short) 1);
                    this.redisService.sAdd(Constants.CACHE_NAME_DEVICE_COMMAND_CACHE, device.getSimCode());
                } else {
                    this.send(enterpriseId, deviceId, messageId, messageBody, gatewayId);
                }
            } else {
                errorList.add(deviceId);
            }
        }
        return errorList;
    }

    @Override
    public List<?> getDeviceDownMessageState(String enterpriseId, String[] ids) throws Exception {
        if (ids == null || ids.length == 0) {
            return null;
        }
        StringBuffer sb = new StringBuffer();
        for (String id : ids) {
            sb.append(",'");
            sb.append(id);
            sb.append("'");
        }
        sb.deleteCharAt(0);
        return this.service.findBySql("select id,state from device_down_message where id in (" + sb.toString() + ") and enterprise_id = ?", enterpriseId);
    }

    @Override
    public void forceOffline(String simCode, String gatewayId) throws Exception {
        Map<String, String> map = new HashMap<String, String>();
        map.put("simCode", simCode);
        String routingKey = gatewayId;
        this.commonDownMessageProducer.send(routingKey, new ExchangeMessage(ExchangeMessage.MESSAGEID_DEVICE_LOGOFF, map, null, gatewayId));
    }

    @Override
    public void removeBlacklist(String id) throws Exception {
        DeviceDataLimitAlarm alarm = (DeviceDataLimitAlarm) this.service.get(DeviceDataLimitAlarm.ENTITY_NAME, id);
        if (alarm != null) {
            Map<String, Object> map = new HashMap<String, Object>();
            String deviceId = alarm.getDeviceId();
            if (StringUtils.isNotBlank(deviceId)) {
                Device device = (Device) this.deviceExtendService.get(deviceId);
                EnterpriseConfig config = (EnterpriseConfig) this.service.get(EnterpriseConfig.ENTITY_NAME, device.getEnterpriseId());
                if (config != null) {
                    map.put("messageId", config.getUpstreamMessageLimit());
                }
                map.put("simCode", device.getSimCode());
            }
            String ip = alarm.getIp();
            if (StringUtils.isNotBlank(ip)) {
                map.put("ip", ip);
            }

            this.commonDownMessageProducer.send(alarm.getGatewayId(), new ExchangeMessage(ExchangeMessage.MESSAGEID_PLATFORM_DOWN_REMOVE_BLACK_LIST_MESSAGE, map, null));
            alarm.setState(DeviceDataLimitAlarm.STATE_DONE);
            this.service.merge(alarm);
        }
    }

    private DeviceDownMessage saveDeviceDownMessage(Device device, String messageId, Map<String, Object> messageBody, Short MessageState) throws Exception {
        DeviceDownMessage message = new DeviceDownMessage();
        message.setDeviceId(device.getId());
        message.setMessageId(messageId);
        message.setEnterpriseId(device.getEnterpriseId());
        message.setMessageBody(JsonUtil.covertObjectToString(messageBody));
        if (MessageState != null) {
            message.setState(MessageState);
        } else {
            int messageSeq = this.redisService.generateSeq(Constants.CACHE_NAME_DEVICE_DOWN_MESSAGE_SEQ_CACHE_PREFIX.concat(device.getSimCode()), MAX_MESSAGE_SEQ);
            message.setMessageSeq(messageSeq);
        }
        message = (DeviceDownMessage) this.service.persist(message);
        return message;
    }

    private DeviceDownMessage isPlaying(String simCode, String messageId, String channelId, String clinetId) throws Exception {
        String cacheKey = simCode.concat("_").concat(messageId).concat("_").concat(channelId);
        ValueWrapper cache = this.cacheManager.getCache(Constants.CACHE_NAME_DEVICE_VIDEO_INFO_CACHE).get(cacheKey);
        if (cache != null && cache.get() != null) {
            Map<?, ?> map = (Map<?, ?>) cache.get();
            int state = (Integer) map.get("state");
            String id = (String) map.get("id");
            // 2：视频监控中；1：视频监控指令已下发；0：平台已下发停止监控指令
            if (channelId.equals(map.get("channelId")) && state != 0) {
                DeviceDownMessage message = (DeviceDownMessage) this.service.get(DeviceDownMessage.ENTITY_NAME, id);
                this.redisService.sAdd(Constants.CACHE_NAME_DEVICE_VIDEO_CLIENT_CACHE.concat(cacheKey), clinetId);
                return message;
            }
        }
        return null;
    }

    private boolean isTalking(String simCode) throws Exception {
        ValueWrapper cache = this.cacheManager.getCache(Constants.CACHE_NAME_DEVICE_VIDEO_INFO_CACHE).get("_talking_".concat(simCode));
        if (cache != null && cache.get() != null) {
            return true;
        }
        return false;
    }

    private void setLiveVideoServer(Map<String, Object> messageBody, String code) throws Exception {
        Map<String, Object> andCondition = new HashMap<String, Object>();
        // 流媒体服务器配置信息
        if (StringUtils.isBlank(code)) {
            if (MESSAGEID_JTT1078_START_VIDEO.equals((String) messageBody.get("messageId"))) {
                andCondition.put("code.eq", "1");
            } else {
                andCondition.put("code.eq", "2");
            }
        } else {
            andCondition.put("code.eq", code);
        }
        andCondition.put("type.eq", 99);
        List<?> gatewayList = this.service.find(Dictionary.ENTITY_NAME, null, false, andCondition);
        if (gatewayList == null || gatewayList.isEmpty()) {
            throw new BizProcessException("无可用流媒体服务器，请联系系统管理员进行配置！");
        }

        Map<?, ?> data = null;
        long max = 0;// 获得最少连接数的流媒体服务器
        for (Object o : gatewayList) {
            Dictionary dictionary = (Dictionary) o;
            Map<?, ?> map = JsonUtil.convertStringToObject(dictionary.getContent(), Map.class);
            String ip = (String) map.get("ip");
            ValueWrapper cache = this.cacheManager.getCache(Constants.CACHE_NAME_VIDEO_GATEWAY_CLIENT_COUNT_CACHE_PREFIX).get(ip);
            if (cache == null) {
                data = map;
                break;
            }
            int count = (Integer) cache.get();
            int limit = (Integer) map.get("limit");
            if (max > count && limit >= count) {
                data = map;
            } else {
                if (max < count) {
                    max = count;
                }
            }
        }
        if (data == null) {
            throw new BizProcessException("无可用流媒体服务器，请联系系统管理员进行配置！");
        }

        messageBody.put("ip", data.get("ip"));
        messageBody.put("udpPort", data.get("udpPort"));
        messageBody.put("tcpPort", data.get("tcpPort"));
    }

    private void setVideoCache(DeviceDownMessage message, String simCode, String channelId, String clinetId, String serverIp) throws Exception {
        String cacheKey = simCode.concat("_").concat(message.getMessageId()).concat("_").concat(channelId);
        this.redisService.sAdd(Constants.CACHE_NAME_DEVICE_VIDEO_LIST_CACHE, cacheKey);
        this.redisService.sAdd(Constants.CACHE_NAME_DEVICE_VIDEO_CLIENT_CACHE.concat(cacheKey), clinetId);
        // 心跳缓存
        this.cacheManager.getCache(Constants.CACHE_NAME_DEVICE_VIDEO_CLIENT_HEARTBEAT_CACHE.concat(message.getMessageId())).put(clinetId, System.currentTimeMillis());
        redisService.expire(Constants.CACHE_NAME_DEVICE_VIDEO_CLIENT_HEARTBEAT_CACHE.concat(message.getMessageId()).concat("::").concat(clinetId), 60);

        Map<String, Object> data = new HashMap<String, Object>();
        data.put("id", message.getId());
        data.put("state", 1);
        data.put("messageSeq", message.getMessageSeq());
        data.put("enterpriseId", message.getEnterpriseId());
        data.put("deviceId", message.getDeviceId());
        data.put("simCode", simCode);
        data.put("messageId", message.getMessageId());
        data.put("channelId", channelId);
        data.put("createTime", System.currentTimeMillis());
        // 登记视频网关数量
        if (StringUtils.isNotBlank(serverIp)) {
            data.put("ip", serverIp);
            this.redisService.incr(Constants.CACHE_NAME_VIDEO_GATEWAY_CLIENT_COUNT_CACHE_PREFIX.concat(serverIp));

            // 设置白名单
            setVideoWhiteList(simCode, 1);
        }
        this.cacheManager.getCache(Constants.CACHE_NAME_DEVICE_VIDEO_INFO_CACHE).put(cacheKey, data);
    }

    @Override
    public List<?> getUploadFileList(String id) throws Exception {
        List<Object> retList = new ArrayList<Object>();
        DeviceHistoryMedia deviceHistoryMedia = (DeviceHistoryMedia) this.service.get(DeviceHistoryMedia.ENTITY_NAME, id);
        if (deviceHistoryMedia == null) {
            return retList;
        }
        FTPClient ftpClient = getFtpClient();
        try {
            ftpClient.enterLocalPassiveMode();
            String base = ftpClient.printWorkingDirectory();
            // ftp服务器与应用服务器操作系统相同时才能使用 File.separator
            if (ftpClient.changeWorkingDirectory(base.concat(deviceHistoryMedia.getFilePath()))) {
                FTPFile[] files = ftpClient.listFiles();
                for (FTPFile file : files) {
                    if (file.isFile()) {
                        Map<String, Object> map = new HashMap<String, Object>();
                        map.put("fileName", file.getName());
                        map.put("fileSize", file.getSize());
                        map.put("createTime", file.getTimestamp().getTimeInMillis());
                        retList.add(map);
                    }
                }
            }
        } finally {
            ftpClient.logout();
        }
        return retList;
    }

    @Override
    public DeviceHistoryMedia uploadFile(String enterpriseId, String deviceId, String messageId, Map<String, Object> messageBody, String gatewayId) throws Exception {
        DeviceHistoryMedia deviceHistoryMedia = new DeviceHistoryMedia();
        FTPClient ftpClient = getFtpClient();
        try {
            ftpClient.enterLocalPassiveMode();
            String base = ftpClient.printWorkingDirectory();

            Device device = (Device) this.deviceExtendService.get(deviceId);
            String filePath = File.separator.concat(device.getSimCode()).concat(File.separator);
            if (!ftpClient.changeWorkingDirectory(base.concat(filePath))) {
                ftpClient.makeDirectory(base.concat(filePath));
            }

            filePath = filePath.concat(DateUtils.format(new Date(), "yyyyMMddHHmmss")).concat(File.separator);
            ftpClient.makeDirectory(base.concat(filePath));

            messageBody.put("filePath", filePath);
            messageBody.putAll(getFtpServer());

            DeviceDownMessage deviceDownMessage = this.send(enterpriseId, deviceId, messageId, messageBody, gatewayId);

            Reflect2Entity.reflect(deviceHistoryMedia, messageBody);
            deviceHistoryMedia.setEnterpriseId(enterpriseId);
            deviceHistoryMedia.setDeviceId(deviceId);
            deviceHistoryMedia.setFilePath(filePath);
            deviceHistoryMedia.setMessageSeq(deviceDownMessage.getMessageSeq());
            this.service.persist(deviceHistoryMedia);
        } finally {
            ftpClient.logout();
        }
        return deviceHistoryMedia;
    }

    @Override
    public DeviceDownMessage uploadControl(String enterpriseId, String deviceId, String messageId, Map<String, Object> messageBody, String gatewayId) throws Exception {
        DeviceDownMessage deviceDownMessage = this.send(enterpriseId, deviceId, messageId, messageBody, gatewayId);
        // 更新状态
        DeviceHistoryMedia deviceHistoryMedia = (DeviceHistoryMedia) this.service.get(DeviceHistoryMedia.ENTITY_NAME, messageBody.get("id"));
        deviceHistoryMedia.setState((Integer.parseInt(String.valueOf(messageBody.get("flag")))));
        service.merge(deviceHistoryMedia);
        return deviceDownMessage;
    }

    @Override
    public DeviceDownMessage queryFileResource(String enterpriseId, String deviceId, String messageId, Map<String, Object> messageBody, String gatewayId) throws Exception {
        DeviceDownMessage deviceDownMessage = this.send(enterpriseId, deviceId, messageId, messageBody, gatewayId);
        DeviceHistoryMedia deviceHistoryMedia = new DeviceHistoryMedia();
        Reflect2Entity.reflect(deviceHistoryMedia, messageBody);
        deviceHistoryMedia.setEnterpriseId(enterpriseId);
        deviceHistoryMedia.setDeviceId(deviceId);
        deviceHistoryMedia.setMessageSeq(deviceDownMessage.getMessageSeq());
        deviceHistoryMedia.setLocationType(DeviceHistoryMedia.LOCATION_TYPE_TERMINAL);// 文件存储类型，终端
        this.service.persist(deviceHistoryMedia);
        return deviceDownMessage;
    }

    /**
     * 初始化ftp服务器
     */
    public FTPClient getFtpClient() throws Exception {
        Map<String, Object> server = getFtpServer();
        if (server == null) {
            throw new Exception("获取FTP服务器失败,请联系管理员进行配置！");
        }
        FTPClient ftpClient = new FTPClient();
        ftpClient.setControlEncoding("utf-8");
        ftpClient.connect((String) server.get("ip"), Integer.parseInt(String.valueOf(server.get("port")))); // 连接ftp服务器
        ftpClient.login((String) server.get("userName"), (String) server.get("password")); // 登录ftp服务器
        int replyCode = ftpClient.getReplyCode(); // 是否成功登录服务器
        if (!FTPReply.isPositiveCompletion(replyCode)) {
            throw new Exception("连接FTP服务器失败");
        }
        return ftpClient;
    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> getFtpServer() throws Exception {
        Map<String, Object> andCondition = Maps.newHashMap();
        andCondition.put("type.eq", 98);
        andCondition.put("code.eq", "ftp_server");
        List<?> list = this.service.find(Dictionary.ENTITY_NAME, null, false, andCondition);
        if (list != null && !list.isEmpty()) {
            Dictionary dictionary = (Dictionary) list.get(0);
            return JsonUtil.convertStringToObject(dictionary.getContent(), Map.class);
        }
        return null;
    }

    private void setVideoWhiteList(String simCode, int type) throws Exception {
        // 取消白名单，todo
        // Map<String, Object> map = Maps.newHashMap();
        // map.put("simCode", simCode);
        // map.put("type", type);
        // this.videoMessageDownProducer.send(JsonUtil.covertObjectToString(map));
    }

}
