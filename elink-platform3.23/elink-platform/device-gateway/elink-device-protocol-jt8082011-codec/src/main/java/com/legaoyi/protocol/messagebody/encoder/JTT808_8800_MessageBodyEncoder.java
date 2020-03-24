package com.legaoyi.protocol.messagebody.encoder;

import java.util.List;

import org.springframework.stereotype.Component;

import com.legaoyi.protocol.down.messagebody.JTT808_8800_MessageBody;
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
@Component(MessageBodyEncoder.MESSAGE_BODY_ENCODER_BEAN_PREFIX + "8800_2011" + MessageBodyEncoder.MESSAGE_BODY_ENCODER_BEAN_SUFFIX)
public class JTT808_8800_MessageBodyEncoder implements MessageBodyEncoder {

    @Override
    public byte[] encode(MessageBody message) throws IllegalMessageException {
        try {
            JTT808_8800_MessageBody messageBody = (JTT808_8800_MessageBody) message;
            MessageBuilder mb = new MessageBuilder();
            mb.addDword(messageBody.getMediaDataId());
            List<Integer> resendPackgeList = messageBody.getResendPackgeList();
            if (resendPackgeList != null && !resendPackgeList.isEmpty()) {
                mb.addByte(resendPackgeList.size());
                for (int packageId : resendPackgeList) {
                    mb.addWord(packageId);
                }
            } else {
                mb.addByte(0);
            }
            return mb.getBytes();
        } catch (Exception e) {
            throw new IllegalMessageException(e);
        }
    }
}
