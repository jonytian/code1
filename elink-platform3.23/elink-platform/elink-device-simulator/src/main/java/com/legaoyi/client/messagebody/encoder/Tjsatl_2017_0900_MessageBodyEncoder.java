package com.legaoyi.client.messagebody.encoder;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.legaoyi.protocol.exception.IllegalMessageException;
import com.legaoyi.protocol.message.MessageBody;
import com.legaoyi.protocol.message.encoder.MessageBodyEncoder;
import com.legaoyi.client.up.messagebody.Tjsatl_2017_0900_MessageBody;
import com.legaoyi.protocol.util.ByteUtils;
import com.legaoyi.protocol.util.MessageBuilder;

/**
 * @author <a href="mailto:shengbo.gao@gmail.com;78772895@qq.com">gaoshengbo</a>
 * @version 1.0.0
 * @since 2018-08-07
 */
@Component(MessageBodyEncoder.MESSAGE_BODY_ENCODER_BEAN_PREFIX + "0900_tjsatl" + MessageBodyEncoder.MESSAGE_BODY_ENCODER_BEAN_SUFFIX)
public class Tjsatl_2017_0900_MessageBodyEncoder implements MessageBodyEncoder {

    @Override
    public byte[] encode(MessageBody message) throws IllegalMessageException {
        try {
            Tjsatl_2017_0900_MessageBody messageBody = (Tjsatl_2017_0900_MessageBody) message;
            MessageBuilder mb = new MessageBuilder();
            mb.addByte(messageBody.getType());

            List<Map<String, Object>> list = messageBody.getMessageList();
            mb.addByte(list.size());
            if (messageBody.getType() == 0xf7) {
                for (Map<String, Object> map : list) {
                    mb.addByte((int) map.get("deviceId"));
                    mb.addByte(5);
                    mb.addByte((int) map.get("state"));
                    mb.addDword((int) map.get("alarm"));
                }
            } else if (messageBody.getType() == 0xf8) {
                for (Map<String, Object> map : list) {
                    mb.addByte((int) map.get("deviceId"));
                    MessageBuilder mb1 = new MessageBuilder();
                    byte[] bytes = ByteUtils.ascii2bytes((String) map.get("enterpriseName"));
                    mb1.addByte(bytes.length);
                    mb1.append(bytes);

                    bytes = ByteUtils.ascii2bytes((String) map.get("productType"));
                    mb1.addByte(bytes.length);
                    mb1.append(bytes);

                    bytes = ByteUtils.ascii2bytes((String) map.get("hardwareVersion"));
                    mb1.addByte(bytes.length);
                    mb1.append(bytes);

                    bytes = ByteUtils.ascii2bytes((String) map.get("softwareVersion"));
                    mb1.addByte(bytes.length);
                    mb1.append(bytes);

                    bytes = ByteUtils.ascii2bytes((String) map.get("deviceId"));
                    mb1.addByte(bytes.length);
                    mb1.append(bytes);

                    bytes = ByteUtils.ascii2bytes((String) map.get("clientCode"));
                    mb1.addByte(bytes.length);
                    mb1.append(bytes);

                    mb.append(mb1.getBytes());
                }
            }

            return mb.getBytes();
        } catch (Exception e) {
            throw new IllegalMessageException(e);
        }
    }
}
