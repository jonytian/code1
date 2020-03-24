package com.legaoyi.file.message.codec;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.legaoyi.protocol.exception.IllegalMessageException;
import com.legaoyi.protocol.message.MessageHeader;
import com.legaoyi.protocol.util.ByteUtils;

/**
 * 
 * @author <a href="mailto:shengbo.gao@gmail.com;78772895@qq.com">gaoshengbo</a>
 * @version 1.0.0
 * @since 2015-01-30
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

    public void decode(byte[] message) throws IllegalMessageException {
        int crcCode, crc;
        try {
            messageHeader = new MessageHeader();
            int offset = 0;
            offset++;// 标识位
            byte[] arr = new byte[2];
            System.arraycopy(message, offset, arr, 0, arr.length);
            messageHeader.setMessageId(ByteUtils.bytes2hex(arr));
            offset += arr.length;
            System.arraycopy(message, offset, arr, 0, arr.length);
            int attr = ByteUtils.word2int(arr);
            String bit = Integer.toBinaryString(attr);
            while (bit.length() < 16) {
                bit = "0" + bit;
            }

            messageHeader.setUndefinedBit(Short.parseShort("" + Integer.valueOf(bit.substring(0, 2), 2)));
            messageHeader.setIsSubpackage((Integer.valueOf(bit.substring(2, 3), 2) == 1 ? true : false));
            messageHeader.setEncrypt(Short.parseShort("" + Integer.valueOf(bit.substring(3, 6), 2)));
            messageHeader.setMessageBodyLength(Integer.valueOf(bit.substring(6), 2));

            offset += arr.length;
            arr = new byte[6];
            System.arraycopy(message, offset, arr, 0, arr.length);
            messageHeader.setSimCode(ByteUtils.bytes2bcd(arr));

            offset += arr.length;
            arr = new byte[2];
            System.arraycopy(message, offset, arr, 0, arr.length);
            messageHeader.setMessageSeq(ByteUtils.word2int(arr));

            offset += arr.length;
            if (messageHeader.getIsSubpackage()) {
                System.arraycopy(message, offset, arr, 0, arr.length);
                messageHeader.setTotalPackage(ByteUtils.word2int(arr));

                offset += arr.length;
                System.arraycopy(message, offset, arr, 0, arr.length);
                messageHeader.setPackageSeq(ByteUtils.word2int(arr));

                offset += arr.length;
            }

            if (messageHeader.getMessageBodyLength() > 0) {
                arr = new byte[messageHeader.getMessageBodyLength()];
                System.arraycopy(message, offset, arr, 0, arr.length);
                messageBody = arr;
            }

            offset += messageHeader.getMessageBodyLength();

            crcCode = ByteUtils.byte2int(message[offset]);
            // crc校验
            crc = 0;
            for (int i = 1, l = message.length - 2; i < l; i++) {
                if (i == 1) {
                    crc = ByteUtils.byte2int(message[i]);
                } else {
                    crc = crc ^ ByteUtils.byte2int(message[i]);
                }
            }
        } catch (Exception e) {
            logger.error("illegal message, message={}", ByteUtils.bytes2hex(message), e);
            throw new IllegalMessageException(e);
        }

        if (crcCode != crc) {
            throw new IllegalMessageException("crc illegal");
        }
    }
}
