package com.legaoyi.protocol.messagebody.encoder;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import com.legaoyi.protocol.downstream.messagebody.Jt808_2019_8600_MessageBody;
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
 * @since 2019-05-20
 */
@Component(MessageBodyEncoder.MESSAGE_BODY_ENCODER_BEAN_PREFIX + "8600_2019" + MessageBodyEncoder.MESSAGE_BODY_ENCODER_BEAN_SUFFIX)
public class Jt808_2019_8600_MessageBodyEncoder implements MessageBodyEncoder {

    @Override
    public byte[] encode(MessageBody messageBody) throws IllegalMessageException {
        try {
            Jt808_2019_8600_MessageBody body = (Jt808_2019_8600_MessageBody) messageBody;
            MessageBuilder mb = new MessageBuilder();
            mb.addByte(body.getType());
            List<Map<String, Object>> areaList = body.getAreaList();
            int size = areaList.size();
            if (size > 0) {
                mb.addByte(size);
                for (Map<String, Object> map : areaList) {
                    mb.addDword((Integer) map.get("areaId"));
                    String attribute = (String) map.get("attribute");
                    attribute = StringUtils.reverse(attribute);
                    mb.addWord(ByteUtils.bin2int(attribute));

                    double centerLat = 1000000 * ((Double) map.get("centerLat"));
                    int lat = (int) centerLat;// lat-latitude(纬度)
                    mb.addDword(lat);

                    double centerLng = 1000000 * ((Double) map.get("centerLng"));
                    int lng = (int) centerLng;// lng-longitude (经度)
                    mb.addDword(lng);

                    mb.addDword((Integer) map.get("radius"));
                    if (attribute.charAt(15) == '1') {
                        mb.append(ByteUtils.bcd2bytes(DateUtils.dateTime2bcd((String) map.get("startTime")),6));
                        mb.append(ByteUtils.bcd2bytes(DateUtils.dateTime2bcd((String) map.get("endTime")),6));
                    }

                    if (attribute.charAt(14) == '1') {
                        mb.addWord((Integer) map.get("limitedSpeed"));
                        mb.addByte((Integer) map.get("durationTime"));
                        mb.addWord((Integer) map.get("nightLimitedSpeed"));
                    }

                    byte[] bytes = ByteUtils.gbk2bytes((String) map.get("name"));
                    mb.addWord(bytes.length);
                    mb.append(bytes);
                }
            }
            return mb.getBytes();
        } catch (Exception e) {
            throw new IllegalMessageException(e);
        }
    }
}
