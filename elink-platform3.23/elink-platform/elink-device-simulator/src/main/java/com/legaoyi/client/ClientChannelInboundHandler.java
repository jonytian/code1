package com.legaoyi.client;

import java.io.IOException;
import java.util.List;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.ReferenceCountUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import com.legaoyi.client.message.Message;
import com.legaoyi.client.message.MessageHeader;
import com.legaoyi.client.message.decoder.MessageDecoder;
import com.legaoyi.client.message.encoder.MessageEncoder;
import com.legaoyi.client.message.handler.MessageHandler;
import com.legaoyi.client.message.sender.MessageSender;
import com.legaoyi.client.up.messagebody.JTT808_0002_2011_MessageBody;
import com.legaoyi.client.up.messagebody.JTT808_0102_2011_MessageBody;
import com.legaoyi.protocol.message.decoder.MessageBodyDecoder;
import com.legaoyi.protocol.message.encoder.MessageBodyEncoder;
import com.legaoyi.protocol.util.ByteUtils;

/**
 * @author <a href="mailto:shengbo.gao@gmail.com;78772895@qq.com">gaoshengbo</a>
 * @version 1.0.0
 * @since 2015-01-30
 */
@ChannelHandler.Sharable
@Component("clientChannelInboundHandler")
public class ClientChannelInboundHandler extends ChannelInboundHandlerAdapter {

    private static final Logger logger = LoggerFactory.getLogger(ClientChannelInboundHandler.class);

    @Value("${elink.device.protocol.version}")
    private String protocolVersion;

    @Value("${elink.device.authCode}")
    private String authCode;

    @Value("${elink.device.simCode}")
    private String simCode;

    @Autowired
    @Qualifier("messageSender")
    private MessageSender messageSender;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws InterruptedException { // (2)
        try {
            ByteBuf buf = (ByteBuf) msg;
            int length = buf.readableBytes();
            byte[] bytes = new byte[length];
            buf.readBytes(bytes);
            if (logger.isInfoEnabled()) {
                logger.info("decode message, message={}", ByteUtils.bytes2hex(bytes));
            }

            Message message = new Message();
            MessageDecoder decoder = new MessageDecoder();
            decoder.decode(bytes);
            byte[] messageBody = decoder.getMessageBody();
            MessageHeader messageHeader = decoder.getMessageHeader();
            String messageId = messageHeader.getMessageId();
            message.setMessageHeader(messageHeader);
            MessageBodyDecoder messageBodyDecoder = SpringBeanUtil.getMessageBodyDecoder(messageId, protocolVersion);
            message.setMessageBody(messageBodyDecoder.decode(messageBody));
            logger.info("**********down message={}", message.toString());
            MessageHandler messageHandler = SpringBeanUtil.getMessageHandler(messageId, protocolVersion);
            messageHandler.handle(ctx, message);
        } catch (Exception e) {
            logger.error("channelRead error.msg={}", msg, e);
        } finally {
            ReferenceCountUtil.release(msg);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        if (cause instanceof IOException) {
            ctx.close();
        }
        logger.error("", cause);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        // send0110(ctx);

        Message message = new Message();
        MessageHeader messageHeader = new MessageHeader();
        messageHeader.setSimCode(simCode);
        messageHeader.setMessageId(JTT808_0102_2011_MessageBody.JTT808_MESSAGE_ID);
        message.setMessageHeader(messageHeader);

        JTT808_0102_2011_MessageBody messageBody = new JTT808_0102_2011_MessageBody();
        messageBody.setAuthCode(authCode);
        message.setMessageBody(messageBody);

        messageSender.send(ctx, message);
    }

    // private void send0110(ChannelHandlerContext ctx) throws Exception{
    // Message message = new Message();
    // MessageHeader messageHeader = new MessageHeader();
    // messageHeader.setMessageId("0110");
    // message.setMessageHeader(messageHeader);
    //
    // JTT808_0110_2011_MessageBody messageBody = new JTT808_0110_2011_MessageBody();
    // PublicKey publicKey =
    // RSAUtil.string2PublicKey("MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAmtjjT5uVhBHLSSjsLEqQU9XLCGu2L38eu9ROOuRKE5zeocJj37efLMVC7i1mTO2mThY+opIAOTRaIeRBcFPBIgZqeMkPCfgxXXeQcQB2I4hltxFQTqL+KHWlX1Gwal7vjUs3Z1jOL2Yyu+I0hv+5ADnNja1G5uZsfNcoDm+i5e5A08d7dUdMcxGBpDqpbt4X6OT+CbhbXlSZDOFw+gfEW69irQXapE31W/m8XztKLlT3P2aKN9y2pmf1TzYsCJqc9hd1tedLJShFk/K63j6G6gOYogTCrGH2MkciXXll5xQ067nJ7WdpDBc2LLiONnnG+vPzKW58w9+ydLOfn9QEcwIDAQAB");
    // String ciphertext = RSAUtil.byte2Base64(RSAUtil.encrypt(JTT808_0110_2011_MessageBody.JTT808_MESSAGE_ID.getBytes(RSAUtil.CHARSET), publicKey));
    // messageBody.setCiphertext(ciphertext);
    // message.setMessageBody(messageBody);
    //
    // MessageBodyEncoder messageBodyEncoder = springBeanUtil.getMessageBodyEncoder(messageHeader.getMessageId(), "2011");
    // List<byte[]> byteList = new MessageEncoder().encode(message, messageBodyEncoder);
    // ctx.writeAndFlush(byteList);
    // }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {

    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) evt;
            if (event.state() == IdleState.ALL_IDLE) {
                Message message = new Message();
                MessageHeader messageHeader = new MessageHeader();
                messageHeader.setSimCode(simCode);
                messageHeader.setMessageId(JTT808_0002_2011_MessageBody.JTT808_MESSAGE_ID);
                message.setMessageHeader(messageHeader);

                JTT808_0002_2011_MessageBody messageBody = new JTT808_0002_2011_MessageBody();
                message.setMessageBody(messageBody);
                MessageBodyEncoder messageBodyEncoder = SpringBeanUtil.getMessageBodyEncoder(message.getMessageHeader().getMessageId(), protocolVersion);
                List<byte[]> byteList = new MessageEncoder().encode(message, messageBodyEncoder);
                ctx.writeAndFlush(byteList);
            }
        }
    }
}
