package com.legaoyi.protocol.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelOutboundHandler;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author <a href="mailto:shengbo.gao@gmail.com;78772895@qq.com">gaoshengbo</a>
 * @version 1.0.0
 * @since 2017-06-02
 */
@Component("server")
public class Server {

    private static final Logger logger = LoggerFactory.getLogger(Server.class);

    private volatile boolean isRunning = false;

    private EventLoopGroup bossGroup = null;

    private EventLoopGroup workerGroup = null;

    @Value("${server.channel.idleTime}")
    private int idleTime = 60;

    @Value("${server.tcp.port}")
    private int port = 6008;

    @Value("${connect.threadPool.size}")
    private int connectThreadPoolSize = 1;

    private static int ioThreadPoolSize = Runtime.getRuntime().availableProcessors() + 1;

    private final ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    @Autowired
    @Qualifier("channelInboundEventHandler")
    private ChannelInboundEventHandler channelInboundEventHandler;

    @Autowired
    @Qualifier("messageDataLimitChannelHandler")
    private MessageDataLimitChannelHandler messageDataLimitChannelHandler;

    @Autowired
    @Qualifier("loggerChannelInbouHandler")
    private LoggerChannelInbouHandler loggerChannelInbouHandler;

    @Autowired
    @Qualifier("loggerChannelOutbouHandler")
    private ChannelOutboundHandler loggerChannelOutbouHandler;

    @Autowired
    @Qualifier("messageCountLimitChannelHandler")
    private MessageCountLimitChannelHandler messageCountLimitChannelHandler;

    @Autowired
    @Qualifier("bytesToMessageChannelHandler")
    private BytesToMessageChannelHandler bytesToMessageChannelHandler;

    @Autowired
    @Qualifier("sessionContextChannelHandler")
    private SessionContextChannelHandler sessionContextChannelHandler;

    @Autowired
    @Qualifier("messagePublishChannelHandler")
    private MessagePublishChannelHandler messagePublishChannelHandler;

    @Autowired
    @Qualifier("autoWrite8001MessageChannelHandler")
    private AutoWrite8001MessageChannelHandler autoWrite8001MessageChannelHandler;

    @Autowired
    @Qualifier("codecEncoder")
    private CodecEncoder codecEncoder;

    private static final Object LOCK = new Object();

    public void start() {
        synchronized (LOCK) {
            if (this.isRunning) {
                throw new IllegalStateException("******server is already started .");
            }
            this.isRunning = true;
        }
        try {
            startServer();
        } catch (Exception e) {
            this.isRunning = false;
            logger.error("******start server error", e);
        }
    }

    public void stop() {
        synchronized (LOCK) {
            if (!this.isRunning) {
                throw new IllegalStateException("******server is not yet started .");
            }
            this.isRunning = false;
        }
        logger.info("******stopping server...");
        try {
            // 断开所有终端的连接
            channelGroup.close().awaitUninterruptibly(5 * 1000);
        } catch (Exception e) {
            logger.error("******channelGroup.close() error ", e);
        }
    }

    private void startServer() throws Exception {
        this.bossGroup = new NioEventLoopGroup(connectThreadPoolSize);
        this.workerGroup = new NioEventLoopGroup(ioThreadPoolSize);
        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class).option(ChannelOption.SO_BACKLOG, 1024).option(ChannelOption.SO_RCVBUF, 1024 * 32).childOption(ChannelOption.SO_KEEPALIVE, true)
                    .childOption(ChannelOption.SO_SNDBUF, 1024 * 32).childHandler(new ChannelInitializer<SocketChannel>() {

                        @Override
                        public void initChannel(SocketChannel ch) throws Exception {
                            final ChannelPipeline pipeline = ch.pipeline();
                            pipeline.addLast("codecEncoder", codecEncoder);
                            pipeline.addLast("loggerChannelOutbouHandler", loggerChannelOutbouHandler);
                            pipeline.addLast("idleStateHandler", new IdleStateHandler(idleTime, 0, 0, TimeUnit.SECONDS));
                            pipeline.addLast("openHandler", new OpenChannelHandler(channelGroup));
                            pipeline.addLast("channelInboundEventHandler", channelInboundEventHandler);
                            pipeline.addLast("codecDecoder", new CodecDecoder());
                            pipeline.addLast("messageDataLimitChannelHandler", messageDataLimitChannelHandler);
                            pipeline.addLast("loggerChannelInbouHandler", loggerChannelInbouHandler);
                            pipeline.addLast("bytesToMessageChannelHandler", bytesToMessageChannelHandler);
                            pipeline.addLast("messageCountLimitChannelHandler", messageCountLimitChannelHandler);
                            pipeline.addLast("sessionContextChannelHandler", sessionContextChannelHandler);
                            pipeline.addLast("messagePublishChannelHandler", messagePublishChannelHandler);
                            pipeline.addLast("autoWrite8001MessageChannelHandler", autoWrite8001MessageChannelHandler);
                        }
                    });

            ChannelFuture channelFuture = serverBootstrap.bind(port).sync();

            if (channelFuture.isSuccess()) {
                logger.info("******server started successfully,server port={}", port);
                Channel channel = channelFuture.channel();
                if (channel != null) {
                    channelGroup.add(channel);
                }
            }

            channelFuture.channel().closeFuture().sync();
            // 断开所有终端的连接
            channelGroup.close().awaitUninterruptibly(5 * 1000);
        } finally {
            // Shut down all event loops to terminate all threads.
            try {
                bossGroup.shutdownGracefully();
            } catch (Exception e) {
                logger.error("******bossGroup.shutdownGracefully() error", e);
            }
            try {
                workerGroup.shutdownGracefully();
            } catch (Exception e) {
                logger.error("****** workerGroup.shutdownGracefully() error", e);
            }
            this.isRunning = false;
            logger.info("******server stoped successfully,server port={}", port);
        }
    }
}
