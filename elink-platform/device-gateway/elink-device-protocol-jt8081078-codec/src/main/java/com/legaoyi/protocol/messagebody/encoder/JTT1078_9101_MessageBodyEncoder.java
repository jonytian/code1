package com.legaoyi.protocol.messagebody.encoder;

import org.springframework.stereotype.Component;

import com.legaoyi.protocol.down.messagebody.JTT1078_9101_MessageBody;
import com.legaoyi.protocol.exception.IllegalMessageException;
import com.legaoyi.protocol.message.MessageBody;
import com.legaoyi.protocol.message.encoder.MessageBodyEncoder;
import com.legaoyi.protocol.util.ByteUtils;
import com.legaoyi.protocol.util.MessageBuilder;

/**
 * 
 * @author <a href="mailto:shengbo.gao@gmail.com;78772895@qq.com">gaoshengbo</a>
 * @version 1.0.0
 * @since 2018-04-09
 */
@Component(MessageBodyEncoder.MESSAGE_BODY_ENCODER_BEAN_PREFIX + "9101_2016" + MessageBodyEncoder.MESSAGE_BODY_ENCODER_BEAN_SUFFIX)
public class JTT1078_9101_MessageBodyEncoder implements MessageBodyEncoder {

    @Override
    public byte[] encode(MessageBody message) throws IllegalMessageException {
        try {
            JTT1078_9101_MessageBody messageBody = (JTT1078_9101_MessageBody) message;
            MessageBuilder mb = new MessageBuilder();
            byte[] bytes = ByteUtils.gbk2bytes(messageBody.getIp());
            mb.addByte(bytes.length);
            mb.append(bytes);
            mb.addWord(messageBody.getTcpPort());
            mb.addWord(messageBody.getUdpPort());
            mb.addByte(messageBody.getChannelId());
            mb.addByte(messageBody.getDataType());
            mb.addByte(messageBody.getStreamType());
            return mb.getBytes();
        } catch (Exception e) {
            throw new IllegalMessageException(e);
        }
    }
}
