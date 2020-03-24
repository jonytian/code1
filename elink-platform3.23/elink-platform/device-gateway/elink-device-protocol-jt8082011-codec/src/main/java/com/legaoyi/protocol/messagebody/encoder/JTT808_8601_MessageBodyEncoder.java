package com.legaoyi.protocol.messagebody.encoder;

import java.util.List;

import org.springframework.stereotype.Component;

import com.legaoyi.protocol.down.messagebody.JTT808_8601_MessageBody;
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
@Component(MessageBodyEncoder.MESSAGE_BODY_ENCODER_BEAN_PREFIX + "8601_2011" + MessageBodyEncoder.MESSAGE_BODY_ENCODER_BEAN_SUFFIX)
public class JTT808_8601_MessageBodyEncoder implements MessageBodyEncoder {

    @Override
    public byte[] encode(MessageBody message) throws IllegalMessageException {
        try {
            JTT808_8601_MessageBody messageBody = (JTT808_8601_MessageBody) message;
            MessageBuilder mb = new MessageBuilder();
            List<Integer> areaIds = messageBody.getAreaIds();
            if (areaIds == null) {
                mb.addByte(0);
            } else {
                mb.addByte(areaIds.size());
                for (int id : areaIds) {
                    mb.addDword(id);
                }
            }
            return mb.getBytes();
        } catch (Exception e) {
            throw new IllegalMessageException(e);
        }
    }
}
