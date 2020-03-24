package com.legaoyi.protocol.messagebody.encoder;

import org.springframework.stereotype.Component;

import com.legaoyi.protocol.down.messagebody.JTT808_8701_C4H_2013_MessageBody;
import com.legaoyi.protocol.exception.IllegalMessageException;
import com.legaoyi.protocol.message.MessageBody;
import com.legaoyi.protocol.message.encoder.MessageBodyEncoder;
import com.legaoyi.protocol.messagebody.encoder.JTT808_8701_2013_MessageBodyEncoder;
import com.legaoyi.protocol.util.ByteUtils;
import com.legaoyi.protocol.util.DateUtils;
import com.legaoyi.protocol.util.MessageBuilder;

/**
 * 
 * @author <a href="mailto:shengbo.gao@gmail.com;78772895@qq.com">gaoshengbo</a>
 * @version 1.0.0
 * @since 2015-01-30
 */
@Component(MessageBodyEncoder.MESSAGE_BODY_ENCODER_BEAN_PREFIX + "8701_C4H_2013" + MessageBodyEncoder.MESSAGE_BODY_ENCODER_BEAN_SUFFIX)
public class JTT808_8701_C4H_2013_MessageBodyEncoder extends JTT808_8701_2013_MessageBodyEncoder {

    @Override
    public byte[] encode(MessageBody message) throws IllegalMessageException {
        try {
            JTT808_8701_C4H_2013_MessageBody messageBody = (JTT808_8701_C4H_2013_MessageBody) message;
            MessageBuilder mb = new MessageBuilder();
            mb.append(ByteUtils.bcd2bytes(DateUtils.dateTime2bcd(messageBody.getRealTime()),6));
            mb.append(ByteUtils.bcd2bytes(DateUtils.dateTime2bcd(messageBody.getInstallTime()),6));
            mb.append(ByteUtils.int2dword(Integer.parseInt("" + messageBody.getInitialMileage() * 10)));
            mb.append(ByteUtils.int2dword(Integer.parseInt("" + messageBody.getMileage() * 10)));
            return mb.getBytes();
        } catch (Exception e) {
            throw new IllegalMessageException(e);
        }
    }
}
