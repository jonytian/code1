package com.legaoyi.protocol.messagebody.encoder;

import org.springframework.stereotype.Component;

import com.legaoyi.protocol.down.messagebody.JTT808_8801_MessageBody;
import com.legaoyi.protocol.exception.IllegalMessageException;
import com.legaoyi.protocol.message.MessageBody;
import com.legaoyi.protocol.message.encoder.MessageBodyEncoder;
import com.legaoyi.protocol.util.ByteUtils;
import com.legaoyi.protocol.util.MessageBuilder;

@Component(MessageBodyEncoder.MESSAGE_BODY_ENCODER_BEAN_PREFIX + "8801_2011" + MessageBodyEncoder.MESSAGE_BODY_ENCODER_BEAN_SUFFIX)
public class JTT808_8801_MessageBodyEncoder implements MessageBodyEncoder {

    @Override
    public byte[] encode(MessageBody message) throws IllegalMessageException {
        try {
            JTT808_8801_MessageBody messageBody = (JTT808_8801_MessageBody) message;
            MessageBuilder mb = new MessageBuilder();
            mb.append(ByteUtils.int2byte(messageBody.getChannelId()));
            if (messageBody.getCommandType() == 0) {
                mb.append(new byte[] {0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0});
                return mb.getBytes();
            } else if (messageBody.getCommandType() == 1) {
                mb.append(ByteUtils.hex2bytes("FFFF"));
            } else {
                mb.append(ByteUtils.int2word(messageBody.getTotalPicture()));
            }

            mb.append(ByteUtils.int2word(messageBody.getExecutionTime()));
            mb.append(ByteUtils.int2byte(messageBody.getSaveFlag()));
            mb.append(ByteUtils.int2byte(messageBody.getResolution()));
            mb.append(ByteUtils.int2byte(messageBody.getQuality()));
            mb.append(ByteUtils.int2byte(messageBody.getLuminance()));
            mb.append(ByteUtils.int2byte(messageBody.getContrast()));
            mb.append(ByteUtils.int2byte(messageBody.getSaturation()));
            mb.append(ByteUtils.int2byte(messageBody.getChromaticity()));
            return mb.getBytes();
        } catch (Exception e) {
            throw new IllegalMessageException(e);
        }
    }
}
