package com.example.logsys.netty;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.logsys.entity.Device;
import com.example.logsys.entity.WebSocketPerssionVerify;
import com.example.logsys.mapper.SystemMapper;
import com.example.logsys.netty.util.ClientMsgProtocol;
import com.example.logsys.netty.util.MsgUtil;
import com.example.logsys.netty.util.ServerHandler;
import com.example.logsys.netty.util.WebSocketConstant;
import com.example.logsys.service.AsyncTaskService;
import com.example.logsys.service.DeviceService;
import com.example.logsys.service.SendMsgService;
import com.example.logsys.util.Tools;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.codec.http.websocketx.*;
import io.netty.handler.codec.json.JsonObjectDecoder;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.AttributeKey;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
@ChannelHandler.Sharable
public class NettyServerHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    /**
     * redis工具类模板
     */
    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 发送消息
     */
    @Autowired
    private SendMsgService sendMsgService;
    private WebSocketServerHandshaker handshaker;


    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        super.userEventTriggered(ctx, evt);

        // 握手成功以后，查询用户未读消息，发送未读消息
        if (evt instanceof WebSocketServerProtocolHandler.HandshakeComplete) {
            AttributeKey<WebSocketPerssionVerify> key = AttributeKey.valueOf("perssion");
            WebSocketPerssionVerify webSocketPerssionVerify = ctx.channel().attr(key).get();
            if (null != webSocketPerssionVerify) {
                String token = webSocketPerssionVerify.getToken();
                String imei = webSocketPerssionVerify.getImei();
                String projectName = webSocketPerssionVerify.getProjectName();
                if (StringUtils.isEmpty(token)) {
//                    sendUserNotLoginMsg(ctx);
                    return;
                }
                if (StringUtils.isEmpty(imei)) {
//                    sendUserNotLoginMsg(ctx);
                    return;
                }
                if (StringUtils.isEmpty(projectName)) {
//                    sendUserNotLoginMsg(ctx);
                    return;
                }

                ctx.channel().attr(key).setIfAbsent(webSocketPerssionVerify);

            } else {
//                sendUserNotLoginMsg(ctx);
                return;
            }
        }


        // 用于触发用户事件，包含触发读空闲、写空闲、读写空闲
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent e = (IdleStateEvent) evt;
            if (e.state() == IdleState.READER_IDLE) {
                log.info("********************读取等待***********");
                ctx.writeAndFlush("读取等待：客户端你在吗...\r\n");
                ctx.close();
            } else if (e.state() == IdleState.WRITER_IDLE) {
                log.info("********************写入等待***********");
                ctx.writeAndFlush("写入等待：客户端你在吗...\r\n");
            } else if (e.state() == IdleState.ALL_IDLE) {
                log.info("********************全部等待***********");
                ctx.writeAndFlush("全部等待：客户端你在吗...\r\n");
            }
        }
        ctx.flush();
    }


    /**
     * 当客户端主动链接服务端的链接后，这个通道就是活跃的了。也就是客户端与服务端建立了通信通道并且可以传输数据
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.info("与客户端建立连接，通道开启！");
        // 添加到channelGroup通道组
        MyChannelHandler.channelGroup.add(ctx.channel());
        SocketChannel channel = (SocketChannel) ctx.channel();
        System.out.println("链接报告开始");
        System.out.println("链接报告信息：有一客户端链接到本服务端");
        System.out.println("链接报告IP:" + channel.localAddress().getHostString());
        System.out.println("链接报告Port:" + channel.localAddress().getPort());
        System.out.println("链接报告完毕");
        // 通知客户端链接建立成功
        String str = "通知客户端链接建立成功" + " " + new Date() + " " + channel.localAddress().getHostString() + "\r\n";
        ByteBuf buf = Unpooled.buffer(str.getBytes().length);
        buf.writeBytes(str.getBytes("GBK"));
        ctx.writeAndFlush(buf);

    }
    /**
     * 当客户端主动断开服务端的链接后，这个通道就是不活跃的。也就是说客户端与服务端的关闭了通信通道并且不可以传输数据
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        log.info("与客户端断开连接，通道关闭！");
        log.info("客户端断开链接" + ctx.channel().localAddress().toString());
        // 更新数据
         ServerHandler.serverHandler.updateUserInfo(ctx);
        // 当有客户端退出后，从channelGroup中移除。
        MyChannelHandler.channelGroup.remove(ctx.channel());
    }
    /**
     * 处理消息
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        //首次连接是FullHttpRequest，处理参数
        if (msg instanceof FullHttpRequest) {
            FullHttpRequest request = (FullHttpRequest) msg;
            if (!request.decoderResult().isSuccess()) {
                DefaultFullHttpResponse httpResponse = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.BAD_REQUEST);
                // 返回应答给客户端
                if (httpResponse.status().code() != 200) {
                    ByteBuf buf = Unpooled.copiedBuffer(httpResponse.status().toString(), CharsetUtil.UTF_8);
                    httpResponse.content().writeBytes(buf);
                    buf.release();
                }
                // 如果是非Keep-Alive，关闭连接
                ChannelFuture f = ctx.channel().writeAndFlush(httpResponse);
                if (httpResponse.status().code() != 200) {
                    f.addListener(ChannelFutureListener.CLOSE);
                }
                return;
            }

            String uri = request.uri();
            Map paramMap=getUrlParams(uri);
            log.info("接收到的参数是："+ JSON.toJSONString(paramMap));
            // 获取用户token
            String token = (String) paramMap.get("token");
            String imei = (String) paramMap.get("imei");
            String projectName = (String) paramMap.get("projectName");
            AttributeKey<WebSocketPerssionVerify> key = AttributeKey.valueOf("perssion");
            WebSocketPerssionVerify webSocketPerssionVerify = ctx.channel().attr(key).get();
            if (null == webSocketPerssionVerify) {
                webSocketPerssionVerify = new WebSocketPerssionVerify();
                webSocketPerssionVerify.setToken(token);
                webSocketPerssionVerify.setImei(imei);
                webSocketPerssionVerify.setProjectName(projectName);
                webSocketPerssionVerify.setConnectTime(new Date());
            }
            ctx.channel().attr(key).setIfAbsent(webSocketPerssionVerify);
            ServerHandler.serverHandler.saveUserInfo(webSocketPerssionVerify,ctx);

            WebSocketServerHandshakerFactory wsFactory = new WebSocketServerHandshakerFactory("ws:/" + ctx.channel() + "/websocket", null, false);
            handshaker = wsFactory.newHandshaker(request);
            if (null == handshaker) {
                WebSocketServerHandshakerFactory.sendUnsupportedVersionResponse(ctx.channel());
            } else {
                handshaker.handshake(ctx.channel(), request);
            }
            return;
        }

        //ws
        if (msg instanceof WebSocketFrame) {
            WebSocketFrame webSocketFrame = (WebSocketFrame) msg;
            //关闭请求
            if (webSocketFrame instanceof CloseWebSocketFrame) {
                handshaker.close(ctx.channel(), (CloseWebSocketFrame) webSocketFrame.retain());
                return;
            }
            //ping请求
            if (webSocketFrame instanceof PingWebSocketFrame) {
                ctx.channel().write(new PongWebSocketFrame(webSocketFrame.content().retain()));
                return;
            }
            //只支持文本格式，不支持二进制消息
            if (!(webSocketFrame instanceof TextWebSocketFrame)) {
                throw new Exception("仅支持文本格式");
            }
            String request = ((TextWebSocketFrame) webSocketFrame).text();
            log.info("************服务端收到：" + request);
            if (!Tools.isJSONValid(request)){
                log.error("数据格式有误，仅支持JSON格式");
                throw new Exception("仅JSON格式");
            }
            // 保存数据
            ServerHandler.serverHandler.saveLogInfo(request, ctx);

//            ClientMsgProtocol clientMsgProtocol = JSON.parseObject(request, ClientMsgProtocol.class);
//            //1请求个人信息
//            if (1 == clientMsgProtocol.getType()) {
            ctx.channel().writeAndFlush(MsgUtil.buildMsgOwner(ctx.channel().id().toString()));
            return;
//            }
//            //群发消息
//            if (2 == clientMsgProtocol.getType()) {
//                TextWebSocketFrame textWebSocketFrame = MsgUtil.buildMsgAll(ctx.channel().id().toString(), clientMsgProtocol.getMsgInfo());
//                ChannelHandler.channelGroup.writeAndFlush(textWebSocketFrame);
//            }

        }

    }

    /**
     * 抓住异常，当发生异常的时候，可以做一些相应的处理，比如打印日志、关闭链接
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        // 更新数据
        ServerHandler.serverHandler.updateUserInfo(ctx);
        ctx.close();
        log.info("异常信息：\r\n" + cause.getMessage());
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, TextWebSocketFrame textWebSocketFrame) throws Exception {



    }


//    /**
//     * 删除channelId: <br>
//
//     */
//    private void removeChannelId(ChannelHandlerContext ctx) {
//        String userId = getUserId(ctx);
//        if (StringUtils.isNotEmpty(userId)) {
//            redisTemplate.opsForSet().remove(WebSocketConstant.BOSS_MSG_CHANNELID + userId, ctx.channel().id());
//        }
//    }
//        /**
//         * 发送用户为登陆消息: <br>
//         */
//        public void sendUserNotLoginMsg(ChannelHandlerContext ctx){
//            removeChannelId(ctx);
////            ChannelFuture future = ctx.writeAndFlush(new CloseWebSocketFrame(InMailMsgResp.USER_NOT_LOGIN, InMailMsgResp.USER_NOT_LOGIN_MSG));
//            future.addListener(ChannelFutureListener.CLOSE);
//        }



    private void sendAllMessage(String message){
        //收到信息后，群发给所有channel
        MyChannelHandler.channelGroup.writeAndFlush( new TextWebSocketFrame(message));
    }

    private static Map getUrlParams(String url){
        Map<String,String> map = new HashMap<>();
        url = url.replace("?",";");
        if (!url.contains(";")){
            return map;
        }
        if (url.split(";").length > 0){
            String[] arr = url.split(";")[1].split("&");
            for (String s : arr){
                String key = s.split("=")[0];
                String value = s.split("=")[1];
                map.put(key,value);
            }
            return  map;

        }else{
            return map;
        }
    }






}




