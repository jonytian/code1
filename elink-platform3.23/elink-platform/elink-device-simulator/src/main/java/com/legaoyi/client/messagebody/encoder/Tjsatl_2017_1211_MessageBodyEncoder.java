package com.legaoyi.client.messagebody.encoder;

import org.springframework.stereotype.Component;

import com.legaoyi.protocol.exception.IllegalMessageException;
import com.legaoyi.protocol.message.MessageBody;
import com.legaoyi.protocol.message.encoder.MessageBodyEncoder;
import com.legaoyi.client.up.messagebody.Tjsatl_2017_1211_MessageBody;
import com.legaoyi.protocol.util.ByteUtils;
import com.legaoyi.protocol.util.MessageBuilder;

/**
 * @author <a href="mailto:shengbo.gao@gmail.com;78772895@qq.com">gaoshengbo</a>
 * @version 1.0.0
 * @since 2018-08-07
 */
@Component(MessageBodyEncoder.MESSAGE_BODY_ENCODER_BEAN_PREFIX + "1211_tjsatl" + MessageBodyEncoder.MESSAGE_BODY_ENCODER_BEAN_SUFFIX)
public class Tjsatl_2017_1211_MessageBodyEncoder implements MessageBodyEncoder {

    @Override
    public byte[] encode(MessageBody message) throws IllegalMessageException {
        try {
            Tjsatl_2017_1211_MessageBody messageBody = (Tjsatl_2017_1211_MessageBody) message;
            MessageBuilder mb = new MessageBuilder();
            byte[] bytes = ByteUtils.gbk2bytes(messageBody.getFileName());
            mb.addByte(bytes.length);
            mb.append(bytes);
            mb.addByte(messageBody.getFileType());
            mb.append(ByteUtils.long2dword(messageBody.getFileSize()));
            return mb.getBytes();
        } catch (Exception e) {
            throw new IllegalMessageException(e);
        }
    }
}
