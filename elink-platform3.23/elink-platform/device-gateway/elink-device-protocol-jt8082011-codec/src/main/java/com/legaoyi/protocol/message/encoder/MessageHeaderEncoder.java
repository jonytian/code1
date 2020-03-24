package com.legaoyi.protocol.message.encoder;

import com.legaoyi.protocol.message.MessageHeader;
import com.legaoyi.protocol.util.ByteUtils;
import com.legaoyi.protocol.util.MessageBuilder;

/**
 * 
 * @author <a href="mailto:shengbo.gao@gmail.com;78772895@qq.com">gaoshengbo</a>
 * @version 1.0.0
 * @since 2015-01-30
 */
public class MessageHeaderEncoder {

    public byte[] encode(MessageHeader message) {
        if (message == null) {
            return null;
        }

        MessageHeader messageHeader = (MessageHeader) message;
        MessageBuilder mb = new MessageBuilder();
        mb.append(ByteUtils.int2word(ByteUtils.hex2int(messageHeader.getMessageId())));
        String s1 = Integer.toBinaryString(messageHeader.getMessageBodyLength());
        while (s1.length() < 10) {
            s1 = "0" + s1;
        }

        String s2 = Integer.toBinaryString(messageHeader.getEncrypt());
        while (s2.length() < 3) {
            s2 = "0" + s2;
        }

        String s3 = messageHeader.getIsSubpackage() ? "1" : "0";

        String s4 = Integer.toBinaryString(messageHeader.getUndefinedBit());
        while (s4.length() < 2) {
            s4 = "0" + s4;
        }

        String s = s4 + s3 + s2 + s1;
        mb.addWord(Integer.valueOf(s, 2));
        mb.append(ByteUtils.bcd2bytes(messageHeader.getSimCode(), 6));
        mb.addWord(messageHeader.getMessageSeq());
        if (messageHeader.getIsSubpackage()) {
            mb.addWord(messageHeader.getTotalPackage());
            mb.addWord(messageHeader.getPackageSeq());
        }
        return mb.getBytes();
    }

    @Override
    public MessageHeader clone() {
        MessageHeader messageHeader = null;
        try {
            messageHeader = (MessageHeader) super.clone();
        } catch (Exception e) {
        }
        return messageHeader;
    }
}
