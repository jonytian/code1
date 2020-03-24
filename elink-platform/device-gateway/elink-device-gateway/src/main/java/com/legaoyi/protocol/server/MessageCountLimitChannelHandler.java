package com.legaoyi.protocol.server;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.legaoyi.gateway.security.SecurityUtil;
import com.legaoyi.protocol.message.Message;
import com.legaoyi.protocol.up.messagebody.JTT808_0100_MessageBody;
import com.legaoyi.protocol.up.messagebody.JTT808_0102_MessageBody;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;

@ChannelHandler.Sharable
@Component("messageCountLimitChannelHandler")
public class MessageCountLimitChannelHandler extends BaseMessageChannelInboundHandler {

    @Autowired
    @Qualifier("dataLimitAlarmHandler")
    private DataLimitAlarmHandler dataLimitAlarmHandler;

    @Autowired
    @Qualifier("securityUtil")
    private SecurityUtil securityUtil;

    @Value("${message.count.limit}")
    private int messageCountLimit;

    @Value("${message.0102.count.limit}")
    private int message0102CountLimit;

    @Value("${message.0100.count.limit}")
    private int message0100CountLimit;

    @Value("${media.queue.message}")
    private String mediaMessage;

    private static Map<String, Object> mediaMessageMap;

    @PostConstruct
    public void init() {
        mediaMessageMap = new HashMap<String, Object>();
        if (!StringUtils.isBlank(mediaMessage)) {
            String[] arr = mediaMessage.split(",");
            for (String messageId : arr) {
                mediaMessageMap.put(messageId, null);
            }
            mediaMessage = null;
        }
    }

    @Override
    protected boolean handle(ChannelHandlerContext ctx, Message message) {
        SessionContext sessionContext = ctx.channel().attr(SessionContext.ATTRIBUTE_SESSION_CONTEXT).get();
        Session session = sessionContext.getCurrentSession();
        String messageId = message.getMessageHeader().getMessageId();
        if (!mediaMessageMap.containsKey(messageId)) {// 多媒体消息不做检测
            List<Map<String, Integer>> messageNumLimitList = session.getMessageNumLimit();
            if (messageNumLimitList == null && messageCountLimit > 0) {
                if (!securityUtil.validate(session, messageCountLimit)) {
                    // 流量异常，断开终端链接
                    dataLimitAlarmHandler.handleDataLimitAlarm(session, 2);
                    return false;
                }
            } else {
                if (messageNumLimitList != null) {
                    for (Map<String, Integer> map : messageNumLimitList) {
                        int limitTime = map.get("time");
                        int limit = map.get("limit");
                        String key = SecurityUtil.ATTRIBUTE_MSG_COUNT_TOKEN_BUCKET_KEY + map.get("type");
                        // 检查流量控制
                        if (limit > 0 && !securityUtil.validate(session, key, 1, limitTime, limit)) {
                            // 流量异常，断开终端链接
                            dataLimitAlarmHandler.handleDataLimitAlarm(session, 2);
                            return false;
                        }
                    }
                }
            }
        }

        String simCode = message.getMessageHeader().getSimCode();
        if (JTT808_0102_MessageBody.MESSAGE_ID.equals(messageId)) {
            // 每十分钟不能超过5次鉴权消息，否则认为异常
            if (message0102CountLimit > 0 && !securityUtil.validateByMessageId(simCode, messageId, message0102CountLimit)) {
                // 流量异常，断开终端链接
                dataLimitAlarmHandler.handleDataLimitAlarm(session, 2);
                return false;
            }
        }
        // 注册消息
        else if (JTT808_0100_MessageBody.MESSAGE_ID.equals(messageId)) {
            // 每十分钟不能超过3次注册消息，否则认为异常
            if (message0100CountLimit > 0 && !securityUtil.validateByMessageId(simCode, messageId, message0100CountLimit)) {
                // 流量异常，断开终端链接
                dataLimitAlarmHandler.handleDataLimitAlarm(session, 2);
                return false;
            }
        }

        Set<String> messageLimit = session.getUpMessageLimit();
        if (messageLimit != null && !messageLimit.contains(messageId)) {
            logger.warn("******上行消息权限受限，网关丢弃消息，message limit,messageId={}", messageId);
            return false;
        }

        return true;
    }
}
