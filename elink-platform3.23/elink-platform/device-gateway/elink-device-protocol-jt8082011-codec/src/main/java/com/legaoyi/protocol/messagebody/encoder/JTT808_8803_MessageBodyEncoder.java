package com.legaoyi.protocol.messagebody.encoder;

import org.springframework.stereotype.Component;

import com.legaoyi.protocol.down.messagebody.JTT808_8803_MessageBody;
import com.legaoyi.protocol.exception.IllegalMessageException;
import com.legaoyi.protocol.message.MessageBody;
import com.legaoyi.protocol.message.encoder.MessageBodyEncoder;
import com.legaoyi.protocol.util.MessageBuilder;

/**
 * 
 * @author <a href="mailto:shengbo.gao@gmail.com;78772895@qq.com">gaoshengbo</a>
 * @version 1.0.0
 * @since 2015-01-30
 */
@Component(MessageBodyEncoder.MESSAGE_BODY_ENCODER_BEAN_PREFIX + "8803_2011" + MessageBodyEncoder.MESSAGE_BODY_ENCODER_BEAN_SUFFIX)
public class JTT808_8803_MessageBodyEncoder extends JTT808_8802_MessageBodyEncoder {

    @Override
    public byte[] encode(MessageBody message) throws IllegalMessageException {
        try {
            JTT808_8803_MessageBody messageBody = (JTT808_8803_MessageBody) message;
            MessageBuilder mb = new MessageBuilder();
            mb.append(super.encode(messageBody));
            mb.addByte(messageBody.getSaveFlag());
            return mb.getBytes();
        } catch (Exception e) {
            throw new IllegalMessageException(e);
        }
    }
}
