package com.legaoyi.protocol.messagebody.encoder;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import com.legaoyi.protocol.down.messagebody.JTT1078_9205_MessageBody;
import com.legaoyi.protocol.exception.IllegalMessageException;
import com.legaoyi.protocol.message.MessageBody;
import com.legaoyi.protocol.message.encoder.MessageBodyEncoder;
import com.legaoyi.protocol.util.ByteUtils;
import com.legaoyi.protocol.util.DateUtils;
import com.legaoyi.protocol.util.MessageBuilder;

/**
 * 
 * @author <a href="mailto:shengbo.gao@gmail.com;78772895@qq.com">gaoshengbo</a>
 * @version 1.0.0
 * @since 2018-04-09
 */
@Component(MessageBodyEncoder.MESSAGE_BODY_ENCODER_BEAN_PREFIX + "9205_2016" + MessageBodyEncoder.MESSAGE_BODY_ENCODER_BEAN_SUFFIX)
public class JTT1078_9205_MessageBodyEncoder implements MessageBodyEncoder {

    @Override
    public byte[] encode(MessageBody message) throws IllegalMessageException {
        try {
            JTT1078_9205_MessageBody messageBody = (JTT1078_9205_MessageBody) message;
            MessageBuilder mb = new MessageBuilder();
            mb.addByte(messageBody.getChannelId());
            String startTime = messageBody.getStartTime();
            if (startTime == null || "".equals(startTime)) {
                startTime = "000000000000";
            } else {
                startTime = DateUtils.dateTime2bcd(startTime);
            }
            mb.append(ByteUtils.bcd2bytes(startTime, 6));

            String endTime = messageBody.getEndTime();
            if (endTime == null || "".equals(endTime)) {
                endTime = "000000000000";
            } else {
                endTime = DateUtils.dateTime2bcd(endTime);
            }
            mb.append(ByteUtils.bcd2bytes(endTime, 6));

            String alarmFlag = messageBody.getAlarmFlag();
            alarmFlag = StringUtils.reverse(alarmFlag);
            for (int i = 0; i < 4; i++) {
                String bitStr = alarmFlag.substring(i * 16, (i + 1) * 16);
                mb.addWord(ByteUtils.bin2int(bitStr));
            }
            mb.addByte(messageBody.getResourceType());
            mb.addByte(messageBody.getStreamType());
            mb.addByte(messageBody.getStoreType());
            return mb.getBytes();
        } catch (Exception e) {
            throw new IllegalMessageException(e);
        }
    }
}
