package com.legaoyi.protocol.messagebody.encoder;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import com.legaoyi.protocol.down.messagebody.JTT808_8606_MessageBody;
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
@Component(MessageBodyEncoder.MESSAGE_BODY_ENCODER_BEAN_PREFIX + "8606_2011" + MessageBodyEncoder.MESSAGE_BODY_ENCODER_BEAN_SUFFIX)
public class JTT808_8606_MessageBodyEncoder implements MessageBodyEncoder {

    @Override
    public byte[] encode(MessageBody message) throws IllegalMessageException {
        try {
            JTT808_8606_MessageBody messageBody = (JTT808_8606_MessageBody) message;
            MessageBuilder mb = new MessageBuilder();
            mb.addDword(messageBody.getRouteId());
            String attribute = messageBody.getAttribute();
            // attribute = StringUtils.reverse(attribute);
            mb.addWord(ByteUtils.bin2int(attribute));

            if (attribute.charAt(15) == '1') {
                mb.append(ByteUtils.bcd2bytes(DateUtils.dateTime2bcd(messageBody.getStartTime()), 6));
                mb.append(ByteUtils.bcd2bytes(DateUtils.dateTime2bcd(messageBody.getEndTime()), 6));
            }

            List<Map<String, Object>> inflectionPointList = messageBody.getInflectionPointList();
            mb.addWord(inflectionPointList.size());
            for (Map<String, Object> map : inflectionPointList) {
                mb.addDword((Integer) map.get("pointId"));
                mb.addDword((Integer) map.get("roadId"));

                double lat = 1000000 * ((Double) map.get("lat"));
                mb.addDword((int) lat);

                double lng = 1000000 * ((Double) map.get("lng"));
                mb.addDword((int) lng);

                mb.addByte((Integer) map.get("roadWidth"));
                String roadAttribute = (String) map.get("roadAttribute");
                mb.addByte(ByteUtils.bin2int(roadAttribute));
                if (roadAttribute.charAt(7) == '1') {
                    mb.addWord((Integer) map.get("ltTravelTime"));
                    mb.addWord((Integer) map.get("gtTravelTime"));
                }
                if (roadAttribute.charAt(6) == '1') {
                    mb.addWord((Integer) map.get("limitedSpeed"));
                    mb.addByte((Integer) map.get("durationTime"));
                }
            }
            return mb.getBytes();
        } catch (Exception e) {
            throw new IllegalMessageException(e);
        }
    }
}
