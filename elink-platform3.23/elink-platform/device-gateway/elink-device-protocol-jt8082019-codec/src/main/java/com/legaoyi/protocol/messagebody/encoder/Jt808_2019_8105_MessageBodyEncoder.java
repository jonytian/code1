package com.legaoyi.protocol.messagebody.encoder;

import org.springframework.stereotype.Component;

import com.legaoyi.protocol.downstream.messagebody.Jt808_2019_8105_MessageBody;
import com.legaoyi.protocol.exception.IllegalMessageException;
import com.legaoyi.protocol.message.MessageBody;
import com.legaoyi.protocol.message.encoder.MessageBodyEncoder;
import com.legaoyi.protocol.util.ByteUtils;
import com.legaoyi.protocol.util.MessageBuilder;

@Component(MessageBodyEncoder.MESSAGE_BODY_ENCODER_BEAN_PREFIX + "8105_2019" + MessageBodyEncoder.MESSAGE_BODY_ENCODER_BEAN_SUFFIX)
public class Jt808_2019_8105_MessageBodyEncoder implements MessageBodyEncoder {

    @Override
    public byte[] encode(MessageBody messageBody) throws IllegalMessageException {
        try {
            Jt808_2019_8105_MessageBody body = (Jt808_2019_8105_MessageBody) messageBody;
            MessageBuilder mb = new MessageBuilder();
            mb.addByte(body.getCommandWord());
            String commandParam = body.getCommandParam();
            if (commandParam != null && !"".equals(commandParam)) {
                mb.append(ByteUtils.gbk2bytes(commandParam));
            }
            return mb.getBytes();
        } catch (Exception e) {
            throw new IllegalMessageException(e);
        }
    }
}
