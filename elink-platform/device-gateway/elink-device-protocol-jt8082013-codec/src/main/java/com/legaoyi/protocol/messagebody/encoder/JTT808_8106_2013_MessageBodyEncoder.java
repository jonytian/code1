package com.legaoyi.protocol.messagebody.encoder;

import java.util.List;

import org.springframework.stereotype.Component;

import com.legaoyi.protocol.down.messagebody.JTT808_8106_2013_MessageBody;
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
@Component(MessageBodyEncoder.MESSAGE_BODY_ENCODER_BEAN_PREFIX + "8106_2013" + MessageBodyEncoder.MESSAGE_BODY_ENCODER_BEAN_SUFFIX)
public class JTT808_8106_2013_MessageBodyEncoder implements MessageBodyEncoder {

    @Override
    public byte[] encode(MessageBody message) throws IllegalMessageException {
        try {
            JTT808_8106_2013_MessageBody messageBody = (JTT808_8106_2013_MessageBody) message;
            MessageBuilder mb = new MessageBuilder();
            List<String> list = messageBody.getParamTypes();
            mb.addByte(list.size());
            for (String type : list) {
                mb.append(ByteUtils.hex2dword(type));
            }
            return mb.getBytes();
        } catch (Exception e) {
            throw new IllegalMessageException(e);
        }
    }
}
