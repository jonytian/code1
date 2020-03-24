package com.legaoyi.file.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInboundHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.AttributeKey;

import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.legaoyi.client.CodecEncoder;

@Component("fileClient")
public class FileClient {

    private final static Logger logger = LoggerFactory.getLogger(FileClient.class);

    private volatile boolean isRunning = false;

    private EventLoopGroup bossGroup = null;

    private static Bootstrap bootstrap = null;

    private int idleTime = 60;

    @Autowired
    @Qualifier("fileClientChannelInboundHandler")
    private ChannelInboundHandler clientInboundHandler;

    @Autowired
    @Qualifier("codecEncoder")
    private CodecEncoder codecEncoder;
    
    public static final AttributeKey<String> ALARMID_SESSION_CONTEXT = AttributeKey.valueOf("alarmId");

    public void start(String serverIp,int serverPort,String alarmId) throws Exception {
        if (isRunning) {
            logger.info("*******client is running*******");
            return;
        }
        logger.info("*******client start,serverIp={},serverPort={}", serverIp, serverPort);
        try {
            bossGroup = new NioEventLoopGroup();
            bootstrap = new Bootstrap();
            bootstrap.group(bossGroup) // 注册线程池
                    .channel(NioSocketChannel.class) // 使用NioSocketChannel来作为连接用的channel类
                    .remoteAddress(new InetSocketAddress(serverIp, serverPort)) // 绑定连接端口和host信息
                    .handler(new ChannelInitializer<SocketChannel>() { // 绑定连接初始化器

                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast("fileIdleStateHandler", new IdleStateHandler(0, 0, idleTime, TimeUnit.SECONDS));
                            ch.pipeline().addLast(codecEncoder);
                            ch.pipeline().addLast(clientInboundHandler);
                            ch.attr(ALARMID_SESSION_CONTEXT).set(alarmId);
                        }
                    }).option(ChannelOption.SO_KEEPALIVE, true).option(ChannelOption.TCP_NODELAY, true);

            ChannelFuture future = bootstrap.connect(serverIp, serverPort).sync();
            if (future.isSuccess()) {
                isRunning = true;
                logger.info("*******client start success*****");
            } else {
                isRunning = false;
                logger.info("*******client start error*****");
                // 此处可以重试连接
            }
            future.channel().closeFuture().sync();
            logger.info("*******client stop success*****");
            isRunning = false;
        } catch (Exception e) {
            isRunning = false;
            logger.info("*******client start error*****");
        } finally {
            bossGroup.shutdownGracefully();
        }
    }

}
