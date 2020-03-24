package com.legaoyi.protocol.messagebody.encoder;

import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Component;

import com.legaoyi.protocol.down.messagebody.JTT808_8602_MessageBody;
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
@Component(MessageBodyEncoder.MESSAGE_BODY_ENCODER_BEAN_PREFIX + "8602_2011" + MessageBodyEncoder.MESSAGE_BODY_ENCODER_BEAN_SUFFIX)
public class JTT808_8602_MessageBodyEncoder implements MessageBodyEncoder {

    @Override
    public byte[] encode(MessageBody message) throws IllegalMessageException {
        try {
            JTT808_8602_MessageBody messageBody = (JTT808_8602_MessageBody) message;
            MessageBuilder mb = new MessageBuilder();
            mb.addByte(messageBody.getType());
            List<Map<String, Object>> areaList = messageBody.getAreaList();
            int size = areaList.size();
            if (size > 0) {
                mb.addByte(size);
                for (Map<String, Object> map : areaList) {
                    mb.addDword((Integer) map.get("areaId"));
                    String attribute = (String) map.get("attribute");
                    //attribute = StringUtils.reverse(attribute);
                    mb.addWord(ByteUtils.bin2int(attribute));

                    double topLeftLat = 1000000 * ((Double) map.get("topLeftLat"));//
                    mb.addDword((int) topLeftLat);
                    
                    double topLeftLng = 1000000 * ((Double) map.get("topLeftLng"));// 经度
                    mb.addDword((int) topLeftLng);

                    double bottomRightLat = 1000000 * ((Double) map.get("bottomRightLat"));//
                    mb.addDword((int) bottomRightLat);
                    
                    double bottomRightLng = 1000000 * ((Double) map.get("bottomRightLng"));// 经度
                    mb.addDword((int) bottomRightLng);

                    if (attribute.charAt(15) == '1') {
                        mb.append(ByteUtils.bcd2bytes(DateUtils.dateTime2bcd((String) map.get("startTime")),6));
                        mb.append(ByteUtils.bcd2bytes(DateUtils.dateTime2bcd((String) map.get("endTime")),6));
                    }

                    if (attribute.charAt(14) == '1') {
                        mb.addWord((Integer) map.get("limitedSpeed"));
                        mb.addByte((Integer) map.get("durationTime"));
                    }
                }
            }
            return mb.getBytes();
        } catch (Exception e) {
            throw new IllegalMessageException(e);
        }
    }
}
