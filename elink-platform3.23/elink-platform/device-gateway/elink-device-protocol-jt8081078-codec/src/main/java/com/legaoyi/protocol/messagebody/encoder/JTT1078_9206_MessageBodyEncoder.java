package com.legaoyi.protocol.messagebody.encoder;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import com.legaoyi.protocol.down.messagebody.JTT1078_9206_MessageBody;
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
@Component(MessageBodyEncoder.MESSAGE_BODY_ENCODER_BEAN_PREFIX + "9206_2016" + MessageBodyEncoder.MESSAGE_BODY_ENCODER_BEAN_SUFFIX)
public class JTT1078_9206_MessageBodyEncoder implements MessageBodyEncoder {

    @Override
    public byte[] encode(MessageBody message) throws IllegalMessageException {
        try {
            JTT1078_9206_MessageBody messageBody = (JTT1078_9206_MessageBody) message;
            MessageBuilder mb = new MessageBuilder();
            byte[] bytes = ByteUtils.gbk2bytes(messageBody.getIp());
            mb.addByte(bytes.length);
            mb.append(bytes);
            mb.addWord(messageBody.getPort());

            bytes = ByteUtils.gbk2bytes(messageBody.getUserName());
            mb.addByte(bytes.length);
            mb.append(bytes);

            bytes = ByteUtils.gbk2bytes(messageBody.getPassword());
            mb.addByte(bytes.length);
            mb.append(bytes);

            bytes = ByteUtils.gbk2bytes(messageBody.getFilePath());
            mb.addByte(bytes.length);
            mb.append(bytes);

            mb.addByte(messageBody.getChannelId());

            String startTime = DateUtils.dateTime2bcd(messageBody.getStartTime());
            mb.append(ByteUtils.bcd2bytes(startTime, 6));

            String endTime = DateUtils.dateTime2bcd(messageBody.getEndTime());
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
            mb.addByte(messageBody.getCondition());
            return mb.getBytes();
        } catch (Exception e) {
            throw new IllegalMessageException(e);
        }
    }
}
