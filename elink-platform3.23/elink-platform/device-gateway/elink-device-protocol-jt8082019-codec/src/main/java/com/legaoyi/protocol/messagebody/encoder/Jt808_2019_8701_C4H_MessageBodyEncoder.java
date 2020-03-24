package com.legaoyi.protocol.messagebody.encoder;

import org.springframework.stereotype.Component;

import com.legaoyi.protocol.downstream.messagebody.Jt808_2019_8701_C4H_MessageBody;
import com.legaoyi.protocol.exception.IllegalMessageException;
import com.legaoyi.protocol.message.MessageBody;
import com.legaoyi.protocol.message.encoder.MessageBodyEncoder;
import com.legaoyi.protocol.messagebody.encoder.Jt808_2019_8701_MessageBodyEncoder;
import com.legaoyi.protocol.util.ByteUtils;
import com.legaoyi.protocol.util.DateUtils;
import com.legaoyi.protocol.util.MessageBuilder;

/**
 * 
 * @author <a href="mailto:shengbo.gao@gmail.com;78772895@qq.com">gaoshengbo</a>
 * @version 1.0.0
 * @since 2019-05-20
 */
@Component(MessageBodyEncoder.MESSAGE_BODY_ENCODER_BEAN_PREFIX + "8701_C4H_2019" + MessageBodyEncoder.MESSAGE_BODY_ENCODER_BEAN_SUFFIX)
public class Jt808_2019_8701_C4H_MessageBodyEncoder extends Jt808_2019_8701_MessageBodyEncoder {

    @Override
    public byte[] encode(MessageBody messageBody) throws IllegalMessageException {
        try {
            Jt808_2019_8701_C4H_MessageBody body = (Jt808_2019_8701_C4H_MessageBody) messageBody;
            MessageBuilder mb = new MessageBuilder();
            mb.append(ByteUtils.bcd2bytes(DateUtils.dateTime2bcd(body.getRealTime()), 6));
            mb.append(ByteUtils.bcd2bytes(DateUtils.dateTime2bcd(body.getInstallTime()), 6));
            mb.append(ByteUtils.int2dword(Integer.parseInt("" + body.getInitialMileage() * 10)));
            mb.append(ByteUtils.int2dword(Integer.parseInt("" + body.getMileage() * 10)));
            return mb.getBytes();
        } catch (Exception e) {
            throw new IllegalMessageException(e);
        }
    }
}
