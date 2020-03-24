package com.legaoyi.client.messagebody.encoder;

import org.springframework.stereotype.Component;

import com.legaoyi.protocol.exception.IllegalMessageException;
import com.legaoyi.protocol.message.MessageBody;
import com.legaoyi.protocol.message.encoder.MessageBodyEncoder;
import com.legaoyi.client.up.messagebody.JTT808_0F10_obd_MessageBody;
import com.legaoyi.protocol.util.ByteUtils;
import com.legaoyi.protocol.util.DateUtils;
import com.legaoyi.protocol.util.MessageBuilder;

/**
 * @author <a href="mailto:shengbo.gao@gmail.com;78772895@qq.com">gaoshengbo</a>
 * @version 1.0.0
 * @since 2018-08-07
 */
@Component(MessageBodyEncoder.MESSAGE_BODY_ENCODER_BEAN_PREFIX + "0F10_obd" + MessageBodyEncoder.MESSAGE_BODY_ENCODER_BEAN_SUFFIX)
public class JTT808_0F10_obd_MessageBodyEncoder implements MessageBodyEncoder {

    @Override
    public byte[] encode(MessageBody message) throws IllegalMessageException {
        try {
            JTT808_0F10_obd_MessageBody messageBody = (JTT808_0F10_obd_MessageBody) message;
            MessageBuilder mb = new MessageBuilder();
            mb.append(ByteUtils.bcd2bytes(DateUtils.timestamp2bcd(messageBody.getGpsTime()), 6));
            mb.addByte(messageBody.getFlag());
            mb.append(ByteUtils.ascii2bytes(messageBody.getCode()));
            return mb.getBytes();
        } catch (Exception e) {
            throw new IllegalMessageException(e);
        }
    }
}
