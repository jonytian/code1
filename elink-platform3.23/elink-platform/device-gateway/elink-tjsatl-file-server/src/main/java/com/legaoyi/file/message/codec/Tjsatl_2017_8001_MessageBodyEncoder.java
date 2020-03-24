package com.legaoyi.file.message.codec;

import org.springframework.stereotype.Component;

import com.legaoyi.file.messagebody.Tjsatl_2017_8001_MessageBody;
import com.legaoyi.protocol.exception.IllegalMessageException;
import com.legaoyi.protocol.message.MessageBody;
import com.legaoyi.protocol.message.encoder.MessageBodyEncoder;
import com.legaoyi.protocol.util.ByteUtils;
import com.legaoyi.protocol.util.MessageBuilder;

/**
 * 
 * @author <a href="mailto:shengbo.gao@gmail.com;78772895@qq.com">gaoshengbo</a>
 * @version 1.0.0
 * @since 2015-01-30
 */
@Component(MessageBodyEncoder.MESSAGE_BODY_ENCODER_BEAN_PREFIX + "8001_tjsatl" + MessageBodyEncoder.MESSAGE_BODY_ENCODER_BEAN_SUFFIX)
public class Tjsatl_2017_8001_MessageBodyEncoder implements MessageBodyEncoder {

    @Override
    public byte[] encode(MessageBody message) throws IllegalMessageException {
        try {
            Tjsatl_2017_8001_MessageBody messageBody = (Tjsatl_2017_8001_MessageBody) message;
            MessageBuilder mb = new MessageBuilder();
            mb.addWord(messageBody.getMessageSeq());
            mb.addWord(ByteUtils.hex2int(messageBody.getMessageId()));
            mb.addByte(messageBody.getResult());
            return mb.getBytes();
        } catch (Exception e) {
            throw new IllegalMessageException(e);
        }
    }
}
