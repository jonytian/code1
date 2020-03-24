package com.legaoyi.protocol.messagebody.encoder;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.legaoyi.protocol.down.messagebody.JTT808_8604_MessageBody;
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
 * @since 2015-01-30
 */
@Component(MessageBodyEncoder.MESSAGE_BODY_ENCODER_BEAN_PREFIX + "8604_2011" + MessageBodyEncoder.MESSAGE_BODY_ENCODER_BEAN_SUFFIX)
public class JTT808_8604_MessageBodyEncoder implements MessageBodyEncoder {

    @Override
    public byte[] encode(MessageBody message) throws IllegalMessageException {
        try {
            JTT808_8604_MessageBody messageBody = (JTT808_8604_MessageBody) message;
            MessageBuilder mb = new MessageBuilder();
            mb.addDword(messageBody.getAreaId());
            String attribute = messageBody.getAttribute();
            //attribute = StringUtils.reverse(attribute);
            mb.addWord(ByteUtils.bin2int(attribute));

            if (attribute.charAt(15) == '1') {
                mb.append(ByteUtils.bcd2bytes(DateUtils.dateTime2bcd(messageBody.getStartTime()),6));
                mb.append(ByteUtils.bcd2bytes(DateUtils.dateTime2bcd(messageBody.getEndTime()),6));
            }

            if (attribute.charAt(14) == '1') {
                mb.addWord(messageBody.getLimitedSpeed());
                mb.addByte(messageBody.getDurationTime());
            }

            List<Map<String, Double>> vertexList = messageBody.getVertexList();
            mb.addWord(vertexList.size());
            for (Map<String, Double> map : vertexList) {
                double lat = 1000000 * (map.get("lat"));
                mb.addDword((int) lat);
                double lng = 1000000 * map.get("lng");
                mb.addDword((int) lng);
            }
            return mb.getBytes();
        } catch (Exception e) {
            throw new IllegalMessageException(e);
        }
    }
}
