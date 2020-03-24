package com.legaoyi.protocol.messagebody.encoder;

import org.springframework.stereotype.Component;

import com.legaoyi.protocol.downstream.messagebody.Jt808_2019_8701_84H_MessageBody;
import com.legaoyi.protocol.exception.IllegalMessageException;
import com.legaoyi.protocol.message.MessageBody;
import com.legaoyi.protocol.message.encoder.MessageBodyEncoder;
import com.legaoyi.protocol.messagebody.encoder.Jt808_2019_8701_MessageBodyEncoder;
import com.legaoyi.protocol.util.ByteUtils;
import com.legaoyi.protocol.util.MessageBuilder;

/**
 * 
 * @author <a href="mailto:shengbo.gao@gmail.com;78772895@qq.com">gaoshengbo</a>
 * @version 1.0.0
 * @since 2019-05-20
 */
@Component(MessageBodyEncoder.MESSAGE_BODY_ENCODER_BEAN_PREFIX + "8701_84H_2019" + MessageBodyEncoder.MESSAGE_BODY_ENCODER_BEAN_SUFFIX)
public class Jt808_2019_8701_84H_MessageBodyEncoder extends Jt808_2019_8701_MessageBodyEncoder {

    @Override
    public byte[] encode(MessageBody messageBody) throws IllegalMessageException {
        try {
            Jt808_2019_8701_84H_MessageBody body = (Jt808_2019_8701_84H_MessageBody) messageBody;
            MessageBuilder mb = new MessageBuilder();
            mb.append(ByteUtils.gb23122bytes(body.getD0Name(), 10));
            mb.append(ByteUtils.gb23122bytes(body.getD1Name(), 10));
            mb.append(ByteUtils.gb23122bytes(body.getD2Name(), 10));
            mb.append(ByteUtils.gb23122bytes(body.getD3Name(), 10));
            mb.append(ByteUtils.gb23122bytes(body.getD4Name(), 10));
            mb.append(ByteUtils.gb23122bytes(body.getD5Name(), 10));
            mb.append(ByteUtils.gb23122bytes(body.getD6Name(), 10));
            mb.append(ByteUtils.gb23122bytes(body.getD7Name(), 10));
            return mb.getBytes();
        } catch (Exception e) {
            throw new IllegalMessageException(e);
        }
    }
}
