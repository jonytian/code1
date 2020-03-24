package com.legaoyi.client.messagebody.encoder;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import com.legaoyi.protocol.exception.IllegalMessageException;
import com.legaoyi.protocol.message.MessageBody;
import com.legaoyi.protocol.message.encoder.MessageBodyEncoder;
import com.legaoyi.client.up.messagebody.JTT1078_1205_MessageBody;
import com.legaoyi.client.up.messagebody.JTT1078_1205_MessageBody.MediaResource;
import com.legaoyi.protocol.util.ByteUtils;
import com.legaoyi.protocol.util.DateUtils;
import com.legaoyi.protocol.util.MessageBuilder;

/**
 * @author <a href="mailto:shengbo.gao@gmail.com;78772895@qq.com">gaoshengbo</a>
 * @version 1.0.0
 * @since 2018-08-07
 */
@Component(MessageBodyEncoder.MESSAGE_BODY_ENCODER_BEAN_PREFIX + "1205_1078" + MessageBodyEncoder.MESSAGE_BODY_ENCODER_BEAN_SUFFIX)
public class JTT1078_1205_MessageBodyEncoder implements MessageBodyEncoder {

    @Override
    public byte[] encode(MessageBody message) throws IllegalMessageException {
        try {
            JTT1078_1205_MessageBody messageBody = (JTT1078_1205_MessageBody) message;
            MessageBuilder mb = new MessageBuilder();
            mb.addWord(messageBody.getMessageSeq());
            int size = messageBody.getResourceList().size();
            mb.addDword(size);

            for (MediaResource resource : messageBody.getResourceList()) {
                mb.addByte(resource.getChannelId());
                mb.append(ByteUtils.bcd2bytes(DateUtils.dateTime2bcd(resource.getStartTime()), 6));
                mb.append(ByteUtils.bcd2bytes(DateUtils.dateTime2bcd(resource.getEndTime()), 6));

                String alarmFlag = resource.getAlarmFlag();
                alarmFlag = StringUtils.reverse(alarmFlag);
                for (int i = 0; i < 4; i++) {
                    String bitStr = alarmFlag.substring(i * 16, (i + 1) * 16);
                    mb.addWord(ByteUtils.bin2int(bitStr));
                }

                mb.addByte(resource.getResourceType());
                mb.addByte(resource.getStreamType());
                mb.addByte(resource.getStoreType());
                mb.addDword(resource.getFileSize());

            }
            return mb.getBytes();
        } catch (Exception e) {
            throw new IllegalMessageException(e);
        }
    }
}
