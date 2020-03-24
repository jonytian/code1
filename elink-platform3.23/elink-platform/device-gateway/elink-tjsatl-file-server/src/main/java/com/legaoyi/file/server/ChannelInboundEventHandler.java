package com.legaoyi.file.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;

@ChannelHandler.Sharable
@Component("channelInboundEventHandler")
public class ChannelInboundEventHandler extends ChannelDuplexHandler {

    private static final Logger logger = LoggerFactory.getLogger(ChannelInboundEventHandler.class);

    private static Map<String, String> activeDevices = new ConcurrentHashMap<String, String>();

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
        if (maxTerminalConnSize > 0 && activeDevices.size() >= maxTerminalConnSize) {
            logger.warn("******网关流量限制，强制断开链接,channelId={},ip={}", channelId, ip);
            ctx.close();
        } else {
            activeDevices.put(channelId, ip);
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        String channelId = ctx.channel().id().asLongText();
        activeDevices.remove(channelId);
        String ip = null;
        try {
            ip = ((InetSocketAddress) ctx.channel().remoteAddress()).getAddress().getHostAddress();
        } catch (Exception e) {

        }
        logger.warn("******设备下线了，channelInactive,close session,channelId={},ip={}", channelId, ip);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        String channelId = ctx.channel().id().asLongText();
        // 客户端异常关闭
        if (cause instanceof IOException) {
            logger.error("******设备链接异常断开，exceptionCaught,close session,channelId={}", channelId);
            ctx.close();
        } else {
            logger.error("******exceptionCaught,channelId={},simCode={}", channelId, cause);
        }
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (IdleStateEvent.class.isAssignableFrom(evt.getClass())) {
            IdleStateEvent event = (IdleStateEvent) evt;
            if (event.state() == IdleState.READER_IDLE) {
                String channelId = ctx.channel().id().asLongText();
                String ip = null;
                try {
                    ip = ((InetSocketAddress) ctx.channel().remoteAddress()).getAddress().getHostAddress();
                } catch (Exception e) {
                }
                logger.warn("******设备超时不发数据，网关强制断开链接，userEventTriggered,close session,channelId={},ip={}", channelId, ip);
                ctx.close();
            }
        }
    }

}
