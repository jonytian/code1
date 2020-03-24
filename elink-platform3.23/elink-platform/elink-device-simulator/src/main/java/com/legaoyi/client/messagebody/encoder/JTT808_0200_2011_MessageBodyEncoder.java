package com.legaoyi.client.messagebody.encoder;

import org.springframework.stereotype.Component;

import com.legaoyi.protocol.exception.IllegalMessageException;
import com.legaoyi.protocol.message.MessageBody;
import com.legaoyi.protocol.message.encoder.MessageBodyEncoder;
import com.legaoyi.client.up.messagebody.JTT808_0200_2011_MessageBody;
import com.legaoyi.protocol.util.ByteUtils;
import com.legaoyi.protocol.util.DateUtils;
import com.legaoyi.protocol.util.MessageBuilder;

/**
 * @author <a href="mailto:shengbo.gao@gmail.com;78772895@qq.com">gaoshengbo</a>
 * @version 1.0.0
 * @since 2018-08-07
 */
@Component(MessageBodyEncoder.MESSAGE_BODY_ENCODER_BEAN_PREFIX + "0200_2011" + MessageBodyEncoder.MESSAGE_BODY_ENCODER_BEAN_SUFFIX)
public class JTT808_0200_2011_MessageBodyEncoder implements MessageBodyEncoder {

    @Override
    public byte[] encode(MessageBody message) throws IllegalMessageException {
        try {
            JTT808_0200_2011_MessageBody messageBody = (JTT808_0200_2011_MessageBody) message;
            MessageBuilder mb = new MessageBuilder();
            mb.addDword((int) messageBody.getAlarm());
            mb.addDword((int) messageBody.getState());
            mb.addDword((int) (messageBody.getLat() * 1000000));
            mb.addDword((int) (messageBody.getLng() * 1000000));
            mb.addWord(messageBody.getAltitude());
            mb.addWord((int) (messageBody.getSpeed() * 10));
            mb.addWord(messageBody.getDirection());
            mb.append(ByteUtils.bcd2bytes(DateUtils.timestamp2bcd(messageBody.getGpsTime()), 6));

            mb.append((byte) 0x01);
            mb.addByte(4);
            mb.addDword((int) (messageBody.getMileage() * 10));

            mb.append((byte) 0x02);
            mb.addByte(2);
            mb.addWord((int) (messageBody.getOilmass() * 10));

            mb.append((byte) 0x03);
            mb.addByte(2);
            mb.addWord((int) (messageBody.getDvrSpeed() * 10));
            return mb.getBytes();
        } catch (Exception e) {
            throw new IllegalMessageException(e);
        }
    }
}
