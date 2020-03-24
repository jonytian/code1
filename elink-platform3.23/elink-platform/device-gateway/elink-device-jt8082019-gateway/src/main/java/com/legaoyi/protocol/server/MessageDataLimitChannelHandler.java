package com.legaoyi.protocol.server;

import java.net.InetSocketAddress;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import com.legaoyi.gateway.security.SecurityUtil;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;

@ChannelHandler.Sharable
@Component("messageDataLimitChannelHandler")
public class MessageDataLimitChannelHandler extends BytesMessageChannelInboundHandler {

    private static final Logger logger = LoggerFactory.getLogger(MessageDataLimitChannelHandler.class);

    @Autowired
    @Qualifier("dataLimitAlarmHandler")
    private DataLimitAlarmHandler dataLimitAlarmHandler;

    @Autowired
    @Qualifier("securityUtil")
    private SecurityUtil securityUtil;

    @Value("${message.data.limit}")
    private int messageDataLimit;

    @Override
    public boolean handle(ChannelHandlerContext ctx, byte[] bytes) {
        // 负载均衡部署时，客户端ip获取不到，获取到的可能是负载均衡服务器的ip，需屏蔽
        try {
            String ip = ((InetSocketAddress) ctx.channel().remoteAddress()).getAddress().getHostAddress();
            // 检查流量控制
            if (messageDataLimit > 0 && !securityUtil.validateByIp(ip, bytes.length, messageDataLimit)) {
                dataLimitAlarmHandler.handleDataLimitAlarm(ip, "", 1);
                logger.warn("******设备流量异常，强制断开链接,channelId={},ip={}", ctx.channel().id().asLongText(), ip);
                ctx.close();
                return false;
            }
        } catch (Exception e) {
            logger.error("", e);
        }

        SessionContext sessionContext = ctx.channel().attr(SessionContext.ATTRIBUTE_SESSION_CONTEXT).get();
        Session session = sessionContext.getCurrentSession();
        List<Map<String, Integer>> messageByteLimitList = session.getMessageByteLimit();
        if (messageByteLimitList == null && messageDataLimit > 0) {
            // 检查流量控制
            if (!securityUtil.validate(session, bytes.length, messageDataLimit)) {
                // 流量异常，断开终端链接
                dataLimitAlarmHandler.handleDataLimitAlarm(session, 1);
                return false;
            }
        } else {
            if (messageByteLimitList != null) {
                for (Map<String, Integer> map : messageByteLimitList) {
                    int limitTime = map.get("time");
                    int limit = map.get("limit");
                    String key = SecurityUtil.ATTRIBUTE_MSG_DATA_TOKEN_BUCKET_KEY + map.get("type");
                    // 检查流量控制
                    if (!securityUtil.validate(session, key, bytes.length, limitTime, limit)) {
                        // 流量异常，断开终端链接
                        dataLimitAlarmHandler.handleDataLimitAlarm(session, 1);
                        return false;
                    }
                }
            }
        }
        return true;
    }
}
