package com.legaoyi.protocol.server;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.legaoyi.common.message.ExchangeMessage;

@Component("dataLimitAlarmHandler")
public class DataLimitAlarmHandler {

    private static final Logger logger = LoggerFactory.getLogger(DataLimitAlarmHandler.class);

    @Autowired
    @Qualifier("commonUpstreamMessageHandler")
    private ServerMessageHandler commonUpstreamMessageHandler;

    @Autowired
    @Qualifier("gatewayCacheManager")
    private GatewayCacheManager gatewayCacheManager;

    /**
     * 
     * @param ip
     * @param simCode
     * @param type 1:上行数据流量超频，2：消息条数超频
     */
    public void handleDataLimitAlarm(String ip, String simCode, int type) {
        try {
            Map<String, Object> data = new HashMap<String, Object>();
            data.put("type", type);
            data.put("simCode", simCode);
            data.put("ip", ip);
            commonUpstreamMessageHandler.handle(new ExchangeMessage(ExchangeMessage.MESSAGEID_DATA_LIMIT_ALARM, data, null));
        } catch (Exception e) {
            logger.error("******发送流量异常告警通知失败，handleDataLimitAlarm error", e);
        }

        if (ip != null) {
            Map<String, Object> info = gatewayCacheManager.getBlackListCache(ip);
            if (info == null) {
                info = new HashMap<String, Object>();
            }
            Integer count = (Integer) info.get("count");
            if (count == null) {
                count = 1;
            }
            // 加入黑名单，限制(count * 5)分钟
            info.put("count", count);
            info.put("limitTime", count * 5 * 60 * 1000);
            info.put("startTime", System.currentTimeMillis());
            try {
                gatewayCacheManager.addBlackListCache(ip, info);
            } catch (Exception e) {
            }
        }
    }

    public void handleDataLimitAlarm(Session session, int type) {
        String ip = null;
        String simCode = null;
        if (session != null) {
            ip = ((InetSocketAddress) session.getChannelHandlerContext().channel().remoteAddress()).getAddress().getHostAddress();
            simCode = session.getSimCode();
        }
        logger.warn("******设备流量异常，网关强制断开链接,data limit force offline,simCode={},ip={}", simCode, ip);
        session.getChannelHandlerContext().close();
        handleDataLimitAlarm(ip, simCode, type);
    }
}
