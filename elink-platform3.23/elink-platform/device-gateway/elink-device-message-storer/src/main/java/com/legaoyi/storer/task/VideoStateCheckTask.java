package com.legaoyi.storer.task;

import java.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.Cache.ValueWrapper;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.legaoyi.common.exception.BizProcessException;
import com.legaoyi.common.message.ExchangeMessage;
import com.legaoyi.common.util.Constants;
import com.legaoyi.common.util.JsonUtil;
import com.legaoyi.mq.MQMessageProducer;
import com.legaoyi.persistence.redis.service.RedisService;
import com.legaoyi.storer.service.DeviceService;
import com.legaoyi.storer.service.DictionaryService;
import com.legaoyi.storer.util.HttpClientUtil;

@Lazy(false)
@EnableScheduling
@Component("videoStateCheckTask")
public class VideoStateCheckTask {

    private static final Logger logger = LoggerFactory.getLogger(VideoStateCheckTask.class);

    private static final long MAX_MESSAGE_SEQ = 65535;

    private static final String MESSAGEID_JTT808_VIDEO = "8801";

    private static final String MESSAGEID_JTT1078_VIDEO = "9101";

    @Autowired
    @Qualifier("redisService")
    private RedisService<?> redisService;

    @Autowired
    @Qualifier("deviceService")
    private DeviceService deviceService;

    @Autowired
    @Qualifier("cacheManager")
    private CacheManager cacheManager;

    @Autowired
    @Qualifier("commonDownMessageProducer")
    private MQMessageProducer producer;

    @Autowired
    @Qualifier("dictionaryService")
    private DictionaryService dictionaryService;

    @Scheduled(cron = "*/5 * * * * ?")
    public void run() {
        List<String> list = this.redisService.sMembers(Constants.CACHE_NAME_DEVICE_VIDEO_LIST_CACHE);
        if (list == null || list.isEmpty()) {
            return;
        }

        List<String> evcardList  = Arrays.asList(
                "346039404143","346039404168","346039404176", "346039404150","346039404127",
                "346039404192","346039404184","346039404291","346039404267", "346039404283",
                "346039404275","346039404226","346039404218","346039404200","346039404259",
                "346039404234","346039404242","346039404325","346039404317","346039404309"
        );
        long time = System.currentTimeMillis();
        for (String cacheKey : list) {
            for (String evcardKey : evcardList) {
                logger.info("******cacheKey："+cacheKey+"evcardKey:"+evcardKey+"*************");
               if( evcardKey.equals(cacheKey) ){
                   logger.info("******成功过滤手机号："+evcardKey+"*************");
                   return;
               }
            }
            List<String> clients = this.redisService.sMembers(Constants.CACHE_NAME_DEVICE_VIDEO_CLIENT_CACHE.concat(cacheKey));
            if (clients != null) {
                String messageId = cacheKey.split("_")[1];
                for (String clinetId : clients) {
                    ValueWrapper cache = this.cacheManager.getCache(Constants.CACHE_NAME_DEVICE_VIDEO_CLIENT_HEARTBEAT_CACHE.concat(messageId)).get(clinetId);
                    boolean bool = false;
                    if (cache != null && cache.get() != null) {
                        long lastActiveTime = (Long) cache.get();
                        bool = (lastActiveTime + 15 * 1000) > time;
                    }
                    if (!bool) {
                        this.cacheManager.getCache(Constants.CACHE_NAME_DEVICE_VIDEO_CLIENT_HEARTBEAT_CACHE.concat(messageId)).evict(clinetId);
                        this.redisService.sRem(Constants.CACHE_NAME_DEVICE_VIDEO_CLIENT_CACHE.concat(cacheKey), clinetId);
                    }
                }
                clients = this.redisService.sMembers(Constants.CACHE_NAME_DEVICE_VIDEO_CLIENT_CACHE.concat(cacheKey));
                if (clients == null || clients.isEmpty()) {
                    // 停止视频
                    stopVideo(cacheKey, false);
                }
            } else {
                // 停止视频
                stopVideo(cacheKey, false);
            }
        }

        // 检测终端是否有推流，如果没有推流，还有用户在看，重新发送指令
        // List<Map<String, Object>> streams = getStreams();
        // for (Map<String, Object> map : streams) {
        // int type = (Integer) map.get("type");
        // // 关闭视频流
        // String simCode = (String) map.get("simCode");
        // for (String cacheKey : list) {
        // if (cacheKey.indexOf(simCode) != -1 && cacheKey.indexOf(MESSAGEID_JTT1078_VIDEO) != -1 && isOnline(simCode)) {
        // // 这里需要检查是否当前视频
        // int channelId = (Integer) map.get("channelId");
        // stopVideo(cacheKey, true);
        // if (type == 1) {
        // start1078Video(simCode, channelId, 1);
        // }
        // break;
        // }
        // }
        // }
    }

    private List<Map<String, Object>> getStreams() {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        try {
            String url = dictionaryService.getMediaStatsUrl();
            String json = HttpClientUtil.send(url);
            Map<?, ?> data = JsonUtil.convertStringToObject(json, Map.class);
            if (data != null) {
                data = (Map<?, ?>) data.get("http-flv");
                List<?> servers = (List<?>) data.get("servers");
                if (servers != null) {
                    for (Object o : servers) {
                        Map<?, ?> server = (Map<?, ?>) o;
                        List<?> applications = (List<?>) server.get("applications");
                        if (applications != null) {
                            for (Object o1 : applications) {
                                Map<?, ?> application = (Map<?, ?>) o1;
                                Map<?, ?> live = (Map<?, ?>) application.get("live");
                                if (live != null) {
                                    List<?> streams = (List<?>) live.get("streams");
                                    if (streams != null) {
                                        for (Object o2 : streams) {
                                            Map<?, ?> stream = (Map<?, ?>) o2;
                                            String active = String.valueOf(stream.get("active"));
                                            String publishing = String.valueOf(stream.get("publishing"));
                                            String name = (String) stream.get("name");
                                            String[] arr = name.split("_");
                                            String simCode = arr[0];
                                            while (simCode.length() < 12) {
                                                simCode = "0" + simCode;
                                            }
                                            int channelId = Integer.parseInt(arr[2]);
                                            Map<String, Object> map = new HashMap<String, Object>();
                                            map.put("simCode", simCode);
                                            map.put("channelId", channelId);

                                            int nclients = Integer.valueOf(String.valueOf(stream.get("nclients")));
                                            if ((active.equals("false") || publishing.equals("false")) && nclients > 0) {
                                                map.put("type", 1);
                                                list.add(map);
                                            } else {
                                                if ((active.equals("true") || publishing.equals("true")) && nclients <= 0) {
                                                    map.put("type", 0);
                                                    list.add(map);
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            logger.error("", e);
        }
        return list;
    }

    @SuppressWarnings("unchecked")
    public void stopVideo(String cacheKey, boolean restart) {
        try {
            ValueWrapper cache = this.cacheManager.getCache(Constants.CACHE_NAME_DEVICE_VIDEO_INFO_CACHE).get(cacheKey);
            Map<?, ?> data = null;
            if (cache != null && cache.get() != null) {
                data = (Map<?, ?>) cache.get();
            }
            // 下发指令
            if (data != null) {
                String deviceId = (String) data.get("deviceId");
                String messageId = (String) data.get("messageId");
                String channelId = (String) data.get("channelId");
                String simCode = (String) data.get("simCode");
                Map<String, Object> messageBody = new HashMap<String, Object>();
                if (MESSAGEID_JTT808_VIDEO.equals(messageId)) {
                    messageBody.put("channelId", channelId);
                    messageBody.put("commandType", 0);
                } else if (MESSAGEID_JTT1078_VIDEO.equals(messageId)) {
                    // String id = (String) data.get("id");
                    messageId = "9102";
                    messageBody.put("channelId", channelId);
                    messageBody.put("command", 0);
                    messageBody.put("commadType", 0);
                    messageBody.put("streamType", 1);
                } else {
                    messageId = "9202";
                    messageBody.put("channelId", channelId);
                    messageBody.put("playbackType", 2);
                    messageBody.put("playTimes", 0);
                    messageBody.put("startTime", "000000000000");
                }

                if (!restart) {
                    // 清理缓存
                    this.redisService.sRem(Constants.CACHE_NAME_DEVICE_VIDEO_LIST_CACHE, cacheKey);
                    this.cacheManager.getCache(Constants.CACHE_NAME_DEVICE_VIDEO_INFO_CACHE).evict(cacheKey);
                }

                String gatewayId = null;
                ValueWrapper gatewayCache = cacheManager.getCache(com.legaoyi.common.util.Constants.CACHE_NAME_DEVICE_GATEWAY_CACHE).get(deviceId);
                if (gatewayCache != null && gatewayCache.get() != null) {
                    gatewayId = (String) gatewayCache.get();
                } else {
                    return;
                }
                ValueWrapper stateCache = cacheManager.getCache(com.legaoyi.common.util.Constants.CACHE_NAME_DEVICE_STATE_CACHE.concat(gatewayId)).get(deviceId);
                if (stateCache == null || stateCache.get() == null) {
                    return;
                }

                Map<String, Object> stateMap = (Map<String, Object>) stateCache.get();
                if (3 != (Integer) stateMap.get("state")) {
                    // 设备已离线
                    return;
                }

                int messageSeq = this.redisService.generateSeq(com.legaoyi.common.util.Constants.CACHE_NAME_DEVICE_DOWN_MESSAGE_SEQ_CACHE_PREFIX.concat(simCode), MAX_MESSAGE_SEQ);
                Map<String, Object> messageHeader = new HashMap<String, Object>();
                messageHeader.put("simCode", simCode);
                messageHeader.put("messageId", messageId);
                messageHeader.put("messageSeq", messageSeq);

                Map<String, Object> message = new HashMap<String, Object>();
                message.put("messageHeader", messageHeader);
                message.put("messageBody", messageBody);
                producer.send(gatewayId, new ExchangeMessage(ExchangeMessage.MESSAGEID_PLATFORM_DOWN_MESSAGE, message, "-1", gatewayId).toString());

                // 删除语音对讲缓存
                this.cacheManager.getCache(Constants.CACHE_NAME_DEVICE_VIDEO_INFO_CACHE).evict("_talking_".concat(simCode));
                logger.info("*******stop video,simCode={}", simCode);
            }
        } catch (Exception e) {
            logger.error("******stopVideo error", e);
        }
    }

    public void start1078Video(String simCode, int channelId, int streamType) {
        try {
            Map<String, Object> device = deviceService.getDeviceInfo(simCode);
            if (device == null) {
                return;
            }

            Map<String, Object> messageBody = new HashMap<String, Object>();
            String messageId = MESSAGEID_JTT1078_VIDEO;
            messageBody.put("channelId", channelId);
            messageBody.put("streamType", streamType);
            messageBody.put("command", 0);
            messageBody.put("commadType", 0);
            setLiveVideoServer(messageBody);

            int messageSeq = this.redisService.generateSeq(com.legaoyi.common.util.Constants.CACHE_NAME_DEVICE_DOWN_MESSAGE_SEQ_CACHE_PREFIX.concat(simCode), MAX_MESSAGE_SEQ);
            Map<String, Object> messageHeader = new HashMap<String, Object>();
            messageHeader.put("simCode", simCode);
            messageHeader.put("messageId", messageId);
            messageHeader.put("messageSeq", messageSeq);

            Map<String, Object> message = new HashMap<String, Object>();
            message.put("messageHeader", messageHeader);
            message.put("messageBody", messageBody);

            String gatewayId = null;
            ValueWrapper gatewayCache = cacheManager.getCache(com.legaoyi.common.util.Constants.CACHE_NAME_DEVICE_GATEWAY_CACHE).get(device.get("deviceId"));
            if (gatewayCache != null && gatewayCache.get() != null) {
                gatewayId = (String) gatewayCache.get();
            } else {
                return;
            }
            producer.send(gatewayId, new ExchangeMessage(ExchangeMessage.MESSAGEID_PLATFORM_DOWN_MESSAGE, message, "-1", gatewayId).toString());
            logger.info("*******start video,simCode={},channelId={}", simCode, channelId);
        } catch (Exception e) {
            logger.error("", e);
        }
    }

    private void setLiveVideoServer(Map<String, Object> messageBody) throws Exception {
        List<Map<String, Object>> gatewayList = dictionaryService.get1078Servers();

        Map<?, ?> data = null;
        long max = 0;// 获得最少连接数的流媒体服务器
        for (Map<String, Object> dictionary : gatewayList) {
            String ip = (String) dictionary.get("ip");
            ValueWrapper cache = this.cacheManager.getCache(Constants.CACHE_NAME_VIDEO_GATEWAY_CLIENT_COUNT_CACHE_PREFIX).get(ip);
            if (cache == null) {
                data = dictionary;
                break;
            }
            int count = (Integer) cache.get();
            int limit = (Integer) dictionary.get("limit");
            if (max > count && limit >= count) {
                data = dictionary;
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

    @SuppressWarnings("unchecked")
    private boolean isOnline(String simCode) {
        try {
            Map<String, Object> device = deviceService.getDeviceInfo(simCode);
            if (device == null) {
                return false;
            }

            String deviceId = (String) device.get(com.legaoyi.storer.util.Constants.MAP_KEY_DEVICE_ID);

            String gatewayId = null;
            ValueWrapper gatewayCache = cacheManager.getCache(com.legaoyi.common.util.Constants.CACHE_NAME_DEVICE_GATEWAY_CACHE).get(deviceId);
            if (gatewayCache != null && gatewayCache.get() != null) {
                gatewayId = (String) gatewayCache.get();
            } else {
                return false;
            }

            ValueWrapper stateCache = cacheManager.getCache(com.legaoyi.common.util.Constants.CACHE_NAME_DEVICE_STATE_CACHE.concat(gatewayId)).get(deviceId);
            if (stateCache == null || stateCache.get() == null) {
                return false;
            }

            Map<String, Object> stateMap = (Map<String, Object>) stateCache.get();
            if (3 != (Integer) stateMap.get("state")) {
                // 设备已离线
                return false;
            }
        } catch (Exception e) {
            logger.error("", e);
            return false;
        }

        return true;
    }
}
