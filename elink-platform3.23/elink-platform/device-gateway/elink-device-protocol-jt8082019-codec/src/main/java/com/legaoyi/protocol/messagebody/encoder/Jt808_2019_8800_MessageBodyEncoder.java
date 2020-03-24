package com.legaoyi.protocol.messagebody.encoder;

import java.util.List;

import org.springframework.stereotype.Component;

import com.legaoyi.protocol.downstream.messagebody.Jt808_2019_8800_MessageBody;
import com.legaoyi.protocol.exception.IllegalMessageException;
import com.legaoyi.protocol.message.MessageBody;
import com.legaoyi.protocol.message.encoder.MessageBodyEncoder;
import com.legaoyi.protocol.util.MessageBuilder;

/**
 * 
 * @author <a href="mailto:shengbo.gao@gmail.com;78772895@qq.com">gaoshengbo</a>
 * @version 1.0.0
 * @since 2019-05-20
 */
@Component(MessageBodyEncoder.MESSAGE_BODY_ENCODER_BEAN_PREFIX + "8800_2019" + MessageBodyEncoder.MESSAGE_BODY_ENCODER_BEAN_SUFFIX)
public class Jt808_2019_8800_MessageBodyEncoder implements MessageBodyEncoder {

    @Override
    public byte[] encode(MessageBody messageBody) throws IllegalMessageException {
        try {
            Jt808_2019_8800_MessageBody body = (Jt808_2019_8800_MessageBody) messageBody;
            MessageBuilder mb = new MessageBuilder();
            mb.addDword(body.getMediaDataId());
            List<Integer> resendPackgeList = body.getResendPackgeList();
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
