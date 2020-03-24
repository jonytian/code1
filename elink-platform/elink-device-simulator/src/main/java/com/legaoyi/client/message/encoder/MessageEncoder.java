package com.legaoyi.client.message.encoder;

import java.util.ArrayList;
import java.util.List;

import com.legaoyi.protocol.exception.IllegalMessageException;
import com.legaoyi.client.message.Message;
import com.legaoyi.protocol.message.MessageBody;
import com.legaoyi.protocol.message.encoder.MessageBodyEncoder;
import com.legaoyi.client.message.MessageHeader;
import com.legaoyi.protocol.util.ByteUtils;
import com.legaoyi.protocol.util.MessageBuilder;

/**
 * 
 * @author <a href="mailto:shengbo.gao@gmail.com;78772895@qq.com">gaoshengbo</a>
 * @version 1.0.0
 * @since 2015-01-30
 */
public class MessageEncoder {

    public List<byte[]> encode(Message message, MessageBodyEncoder messageBodyEncoder) throws IllegalMessageException {
        if (message == null) {
            return null;
        }

        Message jtt = (Message) message;
        MessageBody messageBody = jtt.getMessageBody();
        MessageHeader messageHeader = jtt.getMessageHeader();
        try {
            byte[] bodyBytes = null;
            if (messageBody != null) {
                bodyBytes = messageBodyEncoder.encode(messageBody);
            }
            int totalPackage = 1;
            if (bodyBytes != null && bodyBytes.length > 0) {
                totalPackage = bodyBytes.length / 1023;
                if (totalPackage == 0 || totalPackage % 1023 != 0) {
                    totalPackage++;
                }
            }

            if (totalPackage > 1) {
                messageHeader.setIsSubpackage(true);
                messageHeader.setTotalPackage(totalPackage);
            } else {
                if (bodyBytes != null && bodyBytes.length > 0) {
                    messageHeader.setMessageBodyLength(bodyBytes.length);
                } else {
                    messageHeader.setMessageBodyLength(0);
                }
            }

            int messageSeq = messageHeader.getMessageSeq();
            List<byte[]> byteList = new ArrayList<byte[]>();
            int offset = 0;
            for (int seq = 0; seq < totalPackage; seq++) {
                byte[] bytes;
                if (messageHeader.getIsSubpackage()) {
                    messageHeader.setPackageSeq(seq + 1);
                    messageHeader.setMessageSeq(messageSeq + seq);
                    int length = (bodyBytes.length - offset) > 1023 ? 1023 : (bodyBytes.length - offset);
                    bytes = new byte[length];
                    System.arraycopy(bodyBytes, offset, bytes, 0, length);
                    offset += length;
                    messageHeader.setMessageBodyLength(length);
                } else {
                    bytes = bodyBytes;
                }

                MessageHeaderEncoder messageHeaderEncoder = new MessageHeaderEncoder();
                MessageBuilder mb = new MessageBuilder();
                mb.append(messageHeaderEncoder.encode(messageHeader));
                if (messageHeader.getMessageBodyLength() > 0) {
                    mb.append(bytes);
                }

                bytes = mb.getBytes();
                int crc = 0;
                for (int i = 0; i < bytes.length; i++) {
                    byte b = bytes[i];
                    if (i == 0) {
                        crc = ByteUtils.byte2int(b);
                    } else {
                        crc = crc ^ ByteUtils.byte2int(b);
                    }
                }
                mb.addByte(crc);

                bytes = mb.getBytes();
                mb = new MessageBuilder();
                mb.append(Message.FLAG);
                for (int i = 0; i < bytes.length; i++) {
                    byte b = bytes[i];
                    if (b == Message.FLAG) {
                        mb.append(Message.ESCAPE_CHARACTER_0X7D);
                        mb.append(Message.ESCAPE_CHARACTER_0X02);
                    } else if (b == Message.ESCAPE_CHARACTER_0X7D) {
                        mb.append(Message.ESCAPE_CHARACTER_0X7D);
                        mb.append(Message.ESCAPE_CHARACTER_0X01);
                    } else {
                        mb.append(b);
                    }
                }
                mb.append(Message.FLAG);
                byteList.add(mb.getBytes());
            }
            return byteList;
        } catch (Exception e) {
            throw new IllegalMessageException(e);
        }
    }
}
