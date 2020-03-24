package com.legaoyi.protocol.messagebody.encoder;

import org.springframework.stereotype.Component;

import com.legaoyi.protocol.down.messagebody.Tjsatl_2017_8801_MessageBody;
import com.legaoyi.protocol.exception.IllegalMessageException;
import com.legaoyi.protocol.message.MessageBody;
import com.legaoyi.protocol.message.encoder.MessageBodyEncoder;
import com.legaoyi.protocol.util.ByteUtils;
import com.legaoyi.protocol.util.MessageBuilder;

@Component(MessageBodyEncoder.MESSAGE_BODY_ENCODER_BEAN_PREFIX + "8801_tjsatl" + MessageBodyEncoder.MESSAGE_BODY_ENCODER_BEAN_SUFFIX)
public class Tjsatl_2017_8801_MessageBodyEncoder implements MessageBodyEncoder {

    @Override
    public byte[] encode(MessageBody message) throws IllegalMessageException {
        try {
            Tjsatl_2017_8801_MessageBody messageBody = (Tjsatl_2017_8801_MessageBody) message;
            MessageBuilder mb = new MessageBuilder();
            mb.addByte(messageBody.getChannelId());
            if (messageBody.getChannelId() <= 0x25) {
                if (messageBody.getCommandType() == 0) {
                    mb.append(new byte[] {0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0});
                    return mb.getBytes();
                } else if (messageBody.getCommandType() == 1) {
                    mb.append(ByteUtils.hex2bytes("FFFF"));
                } else {
                    mb.append(ByteUtils.int2word(messageBody.getTotalPicture()));
                }

                mb.append(ByteUtils.int2word(messageBody.getExecutionTime()));
                mb.addByte(messageBody.getSaveFlag());
                mb.addByte(messageBody.getResolution());
                mb.addByte(messageBody.getQuality());
                mb.addByte(messageBody.getLuminance());
                mb.addByte(messageBody.getContrast());
                mb.addByte(messageBody.getSaturation());
                mb.addByte(messageBody.getChromaticity());
            }
            return mb.getBytes();
        } catch (Exception e) {
            throw new IllegalMessageException(e);
        }
    }
}
