package com.legaoyi.protocol.messagebody.encoder;

import org.springframework.stereotype.Component;

import com.legaoyi.protocol.downstream.messagebody.Jt808_2019_8801_MessageBody;
import com.legaoyi.protocol.exception.IllegalMessageException;
import com.legaoyi.protocol.message.MessageBody;
import com.legaoyi.protocol.message.encoder.MessageBodyEncoder;
import com.legaoyi.protocol.util.ByteUtils;
import com.legaoyi.protocol.util.MessageBuilder;

@Component(MessageBodyEncoder.MESSAGE_BODY_ENCODER_BEAN_PREFIX + "8801_2019" + MessageBodyEncoder.MESSAGE_BODY_ENCODER_BEAN_SUFFIX)
public class Jt808_2019_8801_MessageBodyEncoder implements MessageBodyEncoder {

    @Override
    public byte[] encode(MessageBody messageBody) throws IllegalMessageException {
        try {
            Jt808_2019_8801_MessageBody body = (Jt808_2019_8801_MessageBody) messageBody;
            MessageBuilder mb = new MessageBuilder();
            mb.append(ByteUtils.int2byte(body.getChannelId()));
            if (body.getCommandType() == 0) {
                mb.append(new byte[] {0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0});
                return mb.getBytes();
            } else if (body.getCommandType() == 1) {
                mb.append(ByteUtils.hex2bytes("FFFF"));
            } else {
                mb.append(ByteUtils.int2word(body.getTotalPicture()));
            }

            mb.append(ByteUtils.int2word(body.getExecutionTime()));
            mb.append(ByteUtils.int2byte(body.getSaveFlag()));
            mb.append(ByteUtils.int2byte(body.getResolution()));
            mb.append(ByteUtils.int2byte(body.getQuality()));
            mb.append(ByteUtils.int2byte(body.getLuminance()));
            mb.append(ByteUtils.int2byte(body.getContrast()));
            mb.append(ByteUtils.int2byte(body.getSaturation()));
            mb.append(ByteUtils.int2byte(body.getChromaticity()));
            return mb.getBytes();
        } catch (Exception e) {
            throw new IllegalMessageException(e);
        }
    }
}
