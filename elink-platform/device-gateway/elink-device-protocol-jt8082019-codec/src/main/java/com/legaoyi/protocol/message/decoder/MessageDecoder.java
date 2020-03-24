package com.legaoyi.protocol.message.decoder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.legaoyi.protocol.exception.IllegalMessageException;
import com.legaoyi.protocol.message.MessageHeader;
import com.legaoyi.protocol.util.ByteUtils;

/**
 * 
 * @author <a href="mailto:shengbo.gao@gmail.com;78772895@qq.com">gaoshengbo</a>
 * @version 1.0.0
 * @since 2019-06-10
 */
public class MessageDecoder {

    private static final Logger logger = LoggerFactory.getLogger(MessageDecoder.class);

    private MessageHeader messageHeader;

    private byte[] messageBody;

    public MessageHeader getMessageHeader() {
        return messageHeader;
    }

    public byte[] getMessageBody() {
        return this.messageBody;
    }

    public void decode(byte[] bytes) throws IllegalMessageException {
        // crc校验
        int crcCode = ByteUtils.byte2int(bytes[1]);
        for (int i = 2, l = bytes.length - 2; i < l; i++) {
            crcCode = crcCode ^ ByteUtils.byte2int(bytes[i]);
        }

        if (crcCode != ByteUtils.byte2int(bytes[bytes.length - 2])) {
            throw new IllegalMessageException("CRC校验失败");
        }

        try {
            messageHeader = decodeMessageHeader(bytes);
            int messageBodyLength = messageHeader.getMessageBodyLength();
            int offset = bytes.length - messageBodyLength - 2;
            // 消息体内容，去掉校验位以及标志位
            messageBody = new byte[messageBodyLength];
            System.arraycopy(bytes, offset, messageBody, 0, messageBody.length);
        } catch (Exception e) {
            logger.error("******illegal message,message={} ", ByteUtils.bytes2hex(bytes), e);
            throw new IllegalMessageException(e);
        }
    }

    private MessageHeader decodeMessageHeader(byte[] bytes) throws Exception {
        MessageHeader messageHeader = new MessageHeader();
        int offset = 0;
        offset++;// 标识位

        byte[] arr = new byte[2];
        System.arraycopy(bytes, offset, arr, 0, arr.length);
        messageHeader.setMessageId(ByteUtils.bytes2hex(arr));
        offset += arr.length;

        System.arraycopy(bytes, offset, arr, 0, arr.length);
        int attr = ByteUtils.word2int(arr);
        String bit = Integer.toBinaryString(attr);
        while (bit.length() < 16) {
            bit = "0" + bit;
        }

        messageHeader.setUndefinedBit(Short.parseShort(bit.substring(0, 1)));// 保留
        // messageHeader.setVersionFlag(Short.parseShort(bit.substring(1, 2)));// 版本标识
        boolean isSubpackage = bit.substring(2, 3).equals("1");
        messageHeader.setIsSubpackage((Integer.valueOf(bit.substring(2, 3), 2) == 1 ? true : false));// 分包标识
        messageHeader.setEncrypt(Short.parseShort("" + Integer.valueOf(bit.substring(3, 6), 2)));// 加密方式

        int messageBodyLength = Integer.valueOf(bit.substring(6), 2);
        messageHeader.setMessageBodyLength(messageBodyLength);// 消息体长度
        offset += arr.length;

        // messageHeader.setVersion(ByteUtils.byteToInt(bytes[offset]));// 协议版本号
        offset++;

        arr = new byte[10];
        System.arraycopy(bytes, offset, arr, 0, arr.length);
        messageHeader.setSimCode(ByteUtils.bytes2bcd(arr));
        offset += arr.length;

        // 消息流水号
        arr = new byte[2];
        System.arraycopy(bytes, offset, arr, 0, arr.length);
        messageHeader.setMessageSeq(ByteUtils.word2int(arr));
        offset += arr.length;

        // 是否存在分包
        if (isSubpackage) {
            System.arraycopy(bytes, offset, arr, 0, arr.length);
            messageHeader.setTotalPackage(ByteUtils.word2int(arr));// 分包总数
            offset += arr.length;

            System.arraycopy(bytes, offset, arr, 0, arr.length);
            messageHeader.setPackageSeq(ByteUtils.word2int(arr));// 当前分包流水号
            offset += arr.length;
        }

        if (messageBodyLength != (bytes.length - offset - 2)) {
            throw new IllegalMessageException("消息体长度不正确");
        }
        return messageHeader;
    }
}
