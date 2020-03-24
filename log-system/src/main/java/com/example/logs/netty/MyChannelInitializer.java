package com.example.logs.netty;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.handler.timeout.IdleStateHandler;


public class MyChannelInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel channel) {
        /**
         * 心跳监测
         * 1、readerIdleTimeSeconds 读超时时间
         * 2、writerIdleTimeSeconds 写超时时间
         * 3、allIdleTimeSeconds    读写超时时间
         * 4、TimeUnit.SECONDS 秒[默认为秒，可以指定]
         */
        channel.pipeline().addLast(new IdleStateHandler(600, 600, 600));
        //websocket协议本身是基于http协议的，所以这边也要使用http解编码器
        channel.pipeline().addLast(new HttpServerCodec());
        //以块的方式来写的处理器
        channel.pipeline().addLast(new ChunkedWriteHandler());
        channel.pipeline().addLast(new HttpObjectAggregator(8192));
//        channel.pipeline().addLast(new WebSocketServerProtocolHandler("/ws", "/websocket", true, 65536 * 10));
        channel.pipeline().addLast(new NettyServerHandler());

        //websocket协议本身是基于http协议的，所以这边也要使用http解编码器
//        ch.pipeline().addLast(new HttpServerCodec());
//        //以块的方式来写的处理器
//        ch.pipeline().addLast(new ChunkedWriteHandler());
//        ch.pipeline().addLast(new HttpObjectAggregator(8192));
//        ch.pipeline().addLast(new WebSocketServerProtocolHandler("/ws", "WebSocket", true, 65536 * 10));
//        ch.pipeline().addLast(new MyWebSocketHandler());

//        channel.pipeline().addLast("http-codec", new HttpServerCodec());
//        channel.pipeline().addLast("aggregator", new HttpObjectAggregator(65536));
//        channel.pipeline().addLast("http-chunked", new ChunkedWriteHandler());
//        // 在管道中添加我们自己的接收数据实现方法
//        channel.pipeline().addLast(new MyWebSocketHandler());

    }

}
