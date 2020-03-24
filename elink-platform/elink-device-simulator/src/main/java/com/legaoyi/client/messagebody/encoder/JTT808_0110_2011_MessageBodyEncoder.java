package com.legaoyi.client.messagebody.encoder;

import org.springframework.stereotype.Component;

import com.legaoyi.protocol.exception.IllegalMessageException;
import com.legaoyi.protocol.message.MessageBody;
import com.legaoyi.protocol.message.encoder.MessageBodyEncoder;
import com.legaoyi.client.up.messagebody.JTT808_0110_2011_MessageBody;
import com.legaoyi.client.util.RSAUtil;
import com.legaoyi.protocol.util.MessageBuilder;

/**
 * @author <a href="mailto:shengbo.gao@gmail.com;78772895@qq.com">gaoshengbo</a>
 * @version 1.0.0
 * @since 2018-08-07
 */
@Component(MessageBodyEncoder.MESSAGE_BODY_ENCODER_BEAN_PREFIX + "0110_2011" + MessageBodyEncoder.MESSAGE_BODY_ENCODER_BEAN_SUFFIX)
public class JTT808_0110_2011_MessageBodyEncoder implements MessageBodyEncoder {

    @Override
    public byte[] encode(MessageBody message) throws IllegalMessageException {
        try {
            JTT808_0110_2011_MessageBody messageBody = (JTT808_0110_2011_MessageBody) message;
            MessageBuilder mb = new MessageBuilder();
            mb.append(messageBody.getCiphertext().getBytes(RSAUtil.CHARSET));
            return mb.getBytes();
        } catch (Exception e) {
            throw new IllegalMessageException(e);
        }
    }
}
