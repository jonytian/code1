package com.legaoyi.protocol.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import com.legaoyi.gateway.security.TokenBucket;
import com.legaoyi.protocol.util.DefaultMessageBuilder;

import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;

@ChannelHandler.Sharable
@Component("channelInboundEventHandler")
public class ChannelInboundEventHandler extends ChannelDuplexHandler {

    private static final Logger logger = LoggerFactory.getLogger(ChannelInboundEventHandler.class);

    @Autowired
    @Qualifier("urgentUpstreamMessageHandler")
    private ServerMessageHandler urgentUpstreamMessageHandler;

    @Autowired
    @Qualifier("dataLimitAlarmHandler")
    private DataLimitAlarmHandler dataLimitAlarmHandler;

    @Value("${elink.copyright.limit}")
    private int maxTerminalConnSize;

    @Autowired
    @Qualifier("gatewayCacheManager")
    private GatewayCacheManager gatewayCacheManager;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        String ip = null;
        try {
            ip = ((InetSocketAddress) ctx.channel().remoteAddress()).getAddress().getHostAddress();
        } catch (Exception e) {

        }
        final String channelId = ctx.channel().id().asLongText();
        logger.warn("******有设备连接上来了，channelActive,channelId={},ip={}", channelId, ip);

        if (ip != null) {
            Map<String, Object> info = gatewayCacheManager.getBlackListCache(ip);
            if (info != null) {
                long startTime = (long) info.get("startTime");
                int limitTime = (int) info.get("limitTime");
                if (System.currentTimeMillis() < (startTime + limitTime)) {
                    logger.warn("******该设备已进黑名单，网关强制断开链接，black list limit,channelId={},ip={}", channelId, ip);
                    ctx.close();
                    return;
                } else {
                    // GatewayCacheManager.removeBlackList(ip);
                }
            }
        }

        // 超过最大连接数，拒绝终端连接并告警
        if (maxTerminalConnSize > 0 && SessionManager.getInstance().size() >= maxTerminalConnSize) {
            dataLimitAlarmHandler.handleDataLimitAlarm(ip, "", 3);
            logger.warn("******网关流量限制，强制断开链接,channelId={},ip={}", channelId, ip);
            ctx.close();
        } else {
            ctx.channel().attr(SessionContext.ATTRIBUTE_SESSION_CONTEXT).set(new SessionContext(ctx));
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        SessionContext sessionContext = ctx.channel().attr(SessionContext.ATTRIBUTE_SESSION_CONTEXT).get();
        Session session = sessionContext.getCurrentSession();
        String simCode = session.getSimCode();
        String channelId = ctx.channel().id().asLongText();
        sessionContext.closeSession(channelId);

        if (!StringUtils.isBlank(simCode)) {
            try {
                urgentUpstreamMessageHandler.handle(DefaultMessageBuilder.buildOfflineMessage(simCode));
            } catch (Exception e) {
                logger.error("******发送设备下线通知消息失败，send offline message error,simCode={}", simCode, e);
            }
            // 流量控制持久化缓存
            Map<String, TokenBucket> tokenBucket = session.getTokenBucket();
            if (null != tokenBucket) {
                gatewayCacheManager.addTokenBucketCache(simCode, tokenBucket);
            }
        }

        if (StringUtils.isBlank(simCode)) {
            simCode = channelId;
        }

        logger.warn("******设备下线了，channelInactive,close session,channelId={},simCode={}", channelId, simCode);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        SessionContext sessionContext = ctx.channel().attr(SessionContext.ATTRIBUTE_SESSION_CONTEXT).get();
        String simCode = sessionContext.getCurrentSession().getSimCode();
        String channelId = ctx.channel().id().asLongText();
        if (StringUtils.isBlank(simCode)) {
            simCode = channelId;
        }

        // 客户端异常关闭
        if (cause instanceof IOException) {
            logger.error("******设备链接异常断开，exceptionCaught,close session,channelId={},simCode={}", channelId, simCode);
            ctx.close();
        } else {
            logger.error("******exceptionCaught,channelId={},simCode={}", channelId, simCode, cause);
        }
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (IdleStateEvent.class.isAssignableFrom(evt.getClass())) {
            IdleStateEvent event = (IdleStateEvent) evt;
            if (event.state() == IdleState.READER_IDLE) {
                SessionContext sessionContext = ctx.channel().attr(SessionContext.ATTRIBUTE_SESSION_CONTEXT).get();
                Session session = sessionContext.getCurrentSession();
                String simCode = session.getSimCode();
                String channelId = ctx.channel().id().asLongText();
                if (StringUtils.isBlank(simCode)) {
                    simCode = channelId;
                }
                logger.warn("******设备超时不发数据，网关强制断开链接，userEventTriggered,close session,channelId={},simCode={}", channelId, simCode);
                ctx.close();
            }
        }
    }

}
