package com.legaoyi.client.messagebody.encoder;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.legaoyi.protocol.exception.IllegalMessageException;
import com.legaoyi.protocol.message.MessageBody;
import com.legaoyi.protocol.message.encoder.MessageBodyEncoder;
import com.legaoyi.client.up.messagebody.Tjsatl_2017_1210_MessageBody;
import com.legaoyi.protocol.util.ByteUtils;
import com.legaoyi.protocol.util.DateUtils;
import com.legaoyi.protocol.util.MessageBuilder;

/**
 * @author <a href="mailto:shengbo.gao@gmail.com;78772895@qq.com">gaoshengbo</a>
 * @version 1.0.0
 * @since 2018-08-07
 */
@Component(MessageBodyEncoder.MESSAGE_BODY_ENCODER_BEAN_PREFIX + "1210_tjsatl" + MessageBodyEncoder.MESSAGE_BODY_ENCODER_BEAN_SUFFIX)
public class Tjsatl_2017_1210_MessageBodyEncoder implements MessageBodyEncoder {

    @Override
    public byte[] encode(MessageBody message) throws IllegalMessageException {
        try {
            Tjsatl_2017_1210_MessageBody messageBody = (Tjsatl_2017_1210_MessageBody) message;
            MessageBuilder mb = new MessageBuilder();
            mb.append(ByteUtils.ascii2bytes(messageBody.getTerminalId(), 7));
            mb.append(ByteUtils.ascii2bytes(messageBody.getTerminalId(), 7));// ?
            mb.append(ByteUtils.bcd2bytes(DateUtils.dateTime2bcd(messageBody.getAlarmTime()), 6));
            mb.addByte(messageBody.getAlarmSeq());
            mb.addByte(messageBody.getTotalFile());
            mb.addByte(messageBody.getAlarmExt());
            mb.append(ByteUtils.ascii2bytes(messageBody.getAlarmId(), 32));
            mb.addByte(messageBody.getType());

            List<Map<String, Object>> list = messageBody.getFileList();
            mb.addByte(list.size());
            for (Map<String, Object> map : list) {
                byte[] bytes = ByteUtils.gbk2bytes((String) map.get("name"));
                mb.addByte(bytes.length);
                mb.append(bytes);
                mb.addDword((int) map.get("size"));
            }

            return mb.getBytes();
        } catch (

        Exception e) {
            throw new IllegalMessageException(e);
        }
    }
}
