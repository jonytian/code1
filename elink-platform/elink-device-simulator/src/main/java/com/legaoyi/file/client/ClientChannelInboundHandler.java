package com.legaoyi.file.client;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.AttributeKey;
import io.netty.util.ReferenceCountUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.legaoyi.client.SpringBeanUtil;
import com.legaoyi.client.down.messagebody.Jt808_2011_8001_MessageBody;
import com.legaoyi.client.message.Message;
import com.legaoyi.client.message.MessageHeader;
import com.legaoyi.client.message.decoder.MessageDecoder;
import com.legaoyi.client.message.sender.MessageSender;
import com.legaoyi.client.up.messagebody.Tjsatl_2017_1210_MessageBody;
import com.legaoyi.client.up.messagebody.Tjsatl_2017_1211_MessageBody;
import com.legaoyi.client.up.messagebody.Tjsatl_2017_1212_MessageBody;
import com.legaoyi.protocol.message.decoder.MessageBodyDecoder;
import com.legaoyi.protocol.util.ByteUtils;
import com.legaoyi.protocol.util.DateUtils;
import com.legaoyi.protocol.util.MessageBuilder;

/**
 * @author <a href="mailto:shengbo.gao@gmail.com;78772895@qq.com">gaoshengbo</a>
 * @version 1.0.0
 * @since 2015-01-30
 */
@ChannelHandler.Sharable
@Component("fileClientChannelInboundHandler")
public class ClientChannelInboundHandler extends ChannelInboundHandlerAdapter {

    private static final Logger logger = LoggerFactory.getLogger(ClientChannelInboundHandler.class);

    @Value("${elink.device.protocol.version}")
    private String protocolVersion;

    @Value("${elink.device.simCode}")
    private String simCode;

    @Autowired
    @Qualifier("messageSender")
    private MessageSender messageSender;

    public static final AttributeKey<String> ALARMID_SESSION_CONTEXT = AttributeKey.valueOf("alarmId");

    public static final AttributeKey<List<String>> FILES_SESSION_CONTEXT = AttributeKey.valueOf("files");

    public static final AttributeKey<Integer> FILES_INDEX_SESSION_CONTEXT = AttributeKey.valueOf("files_index");

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws InterruptedException {
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

            if (messageId.equals(Jt808_2011_8001_MessageBody.JTT808_MESSAGE_ID)) {
                Jt808_2011_8001_MessageBody body = (Jt808_2011_8001_MessageBody) message.getMessageBody();
                if (body.getMessageId().equals(Tjsatl_2017_1210_MessageBody.JTT808_MESSAGE_ID)) {
                    List<String> files = ctx.channel().attr(FILES_SESSION_CONTEXT).get();
                    Integer index = ctx.channel().attr(FILES_INDEX_SESSION_CONTEXT).get();
                    // 发送1211
                    if (files.size() > index) {
                        send1211Message(ctx, files.get(index), message);
                    }
                } else if (body.getMessageId().equals(Tjsatl_2017_1211_MessageBody.JTT808_MESSAGE_ID)) {
                    List<String> files = ctx.channel().attr(FILES_SESSION_CONTEXT).get();
                    Integer index = ctx.channel().attr(FILES_INDEX_SESSION_CONTEXT).get();
                    if (files.size() > index) {
                        // 发送文件数据
                        sendFileData(ctx, files.get(index));
                        send1212Message(ctx, files.get(index), message);
                        index++;
                        ctx.channel().attr(FILES_INDEX_SESSION_CONTEXT).set(index);
                    }
                }
            }
        } catch (Exception e) {
            logger.error("channelRead error.msg={}", msg, e);
        } finally {
            ReferenceCountUtil.release(msg);
        }
    }

    private void send1211Message(ChannelHandlerContext ctx, String fileName, Message message) throws Exception {
        message.getMessageHeader().setMessageId(Tjsatl_2017_1211_MessageBody.JTT808_MESSAGE_ID);
        Tjsatl_2017_1211_MessageBody messageBody = new Tjsatl_2017_1211_MessageBody();
        messageBody.setFileName(fileName);
        messageBody.setFileSize(595l);
        messageBody.setFileType(3);
        message.setMessageBody(messageBody);
        messageSender.send(ctx, message);
    }

    private void send1212Message(ChannelHandlerContext ctx, String fileName, Message message) throws Exception {
        message.getMessageHeader().setMessageId(Tjsatl_2017_1212_MessageBody.JTT808_MESSAGE_ID);
        Tjsatl_2017_1212_MessageBody messageBody = new Tjsatl_2017_1212_MessageBody();
        messageBody.setFileName(fileName);
        messageBody.setFileSize(595l);
        messageBody.setFileType(3);
        message.setMessageBody(messageBody);
        messageSender.send(ctx, message);
    }

    private void sendFileData(ChannelHandlerContext ctx, String fileName) throws Exception {
        List<byte[]> byteList = new ArrayList<byte[]>();
        MessageBuilder mb = new MessageBuilder();

        byte[] bytes = new byte[] {0x30, 0x31, 0x63, 0x64};
        mb.append(bytes);
        mb.append(ByteUtils.gbk2bytes(fileName, 50));
        bytes = mb.getBytes();

        int size = 10;
        byte[] data = getFileData();
        int count = data.length / size;
        int mod = data.length % size;
        if (mod != 0) {
            count++;
        }

        int offset = 0;
        for (int i = 0; i < count; i++) {
            mb = new MessageBuilder();
            mb.append(bytes);
            mb.addDword(offset);

            int length = size;
            if (offset + size > data.length) {
                length = data.length - offset;
            }

            mb.addDword(length);

            byte[] arr = new byte[length];
            System.arraycopy(data, offset, arr, 0, arr.length);
            offset += arr.length;
            mb.append(arr);

            logger.info("**********offset={},length={},data={}", offset, length, ByteUtils.bytes2hex(arr));

            byteList.add(mb.getBytes());
        }
        ctx.writeAndFlush(byteList);
    }

    private byte[] getFileData() throws Exception {
        int total = 50;
        MessageBuilder mb0 = new MessageBuilder();
        for (int i = 0; i < total; i++) {
            MessageBuilder mb = new MessageBuilder();
            mb.addDword(total);
            mb.addDword(i);
            mb.addDword(getRandom(1, 10000));
            mb.addDword(getRandom(1, 10000));
            mb.addDword(getRandom(80000000, 120000000));
            mb.addDword(getRandom(80000000, 120000000));
            mb.addWord(getRandom(0, 1000));
            mb.addWord(getRandom(0, 150));
            mb.addWord(getRandom(0, 359));
            mb.append(ByteUtils.bcd2bytes(DateUtils.timestamp2bcd(System.currentTimeMillis()), 6));
            mb.addWord(getRandom(0, 10) * 100);
            mb.addWord(getRandom(0, 10) * 100);
            mb.addWord(getRandom(0, 10) * 100);
            mb.addWord(getRandom(0, 10) * 100);
            mb.addWord(getRandom(0, 10) * 100);
            mb.addWord(getRandom(0, 10) * 100);
            mb.addWord(getRandom(0, 150));
            mb.addWord(getRandom(0, 150));
            mb.addByte(getRandom(0, 11));
            mb.addByte(getRandom(0, 100));
            mb.addByte(getRandom(0, 100));
            mb.addByte(getRandom(0, 1));
            mb.addWord(getRandom(0, 5000));
            mb.addWord(getRandom(0, 360) + 360);
            mb.addByte(getRandom(0, 2));
            mb.addWord(getRandom(0, 1000));

            byte[] bytes = mb.getBytes();
            int code = 0;
            for (int j = 0; j < bytes.length; j++) {
                code += ByteUtils.byte2int(bytes[j]);
            }
            mb0.append(bytes);
            mb0.addByte(code & 0xff);
        }
        return mb0.getBytes();
    }

    private static int getRandom(int start, int end) {
        return (int) (start + Math.random() * end);
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

        String alarmId = ctx.channel().attr(ALARMID_SESSION_CONTEXT).get();

        List<String> files = new ArrayList<String>();

        // 发送附件上传消息1210
        Message message = new Message();
        MessageHeader messageHeader = new MessageHeader();
        messageHeader.setSimCode(simCode);
        messageHeader.setMessageId(Tjsatl_2017_1210_MessageBody.JTT808_MESSAGE_ID);
        message.setMessageHeader(messageHeader);

        Tjsatl_2017_1210_MessageBody messageBody = new Tjsatl_2017_1210_MessageBody();
        messageBody.setTerminalId("1234567");
        messageBody.setAlarmTime("2019-10-03 20:00:08");
        messageBody.setAlarmSeq(25);
        messageBody.setAlarmExt(1);
        messageBody.setTotalFile(2);
        messageBody.setAlarmId(UUID.randomUUID().toString().replaceAll("-", ""));
        messageBody.setType(0);

        String fileName = "03_65_6401_" + getRandom(1, 100) + "_" + alarmId + ".bin";
        files.add(fileName);

        List<Map<String, Object>> fileList = new ArrayList<Map<String, Object>>();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("name", fileName);
        map.put("size", 1234598);
        fileList.add(map);

        fileName = "03_65_6401_" + getRandom(1, 100) + "_" + alarmId + ".bin";
        files.add(fileName);

        ctx.channel().attr(FILES_SESSION_CONTEXT).set(files);
        ctx.channel().attr(FILES_INDEX_SESSION_CONTEXT).set(0);

        map = new HashMap<String, Object>();
        map.put("name", fileName);
        map.put("size", 1234431);
        fileList.add(map);

        messageBody.setFileList(fileList);

        message.setMessageBody(messageBody);

        messageSender.send(ctx, message);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {

    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) evt;
            if (event.state() == IdleState.ALL_IDLE) {
                ctx.close();
            }
        }
    }
}
