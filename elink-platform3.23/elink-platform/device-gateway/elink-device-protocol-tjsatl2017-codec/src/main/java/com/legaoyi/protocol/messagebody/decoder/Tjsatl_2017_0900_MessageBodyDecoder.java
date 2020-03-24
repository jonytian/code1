package com.legaoyi.protocol.messagebody.decoder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.legaoyi.protocol.exception.IllegalMessageException;
import com.legaoyi.protocol.message.MessageBody;
import com.legaoyi.protocol.message.decoder.MessageBodyDecoder;
import com.legaoyi.protocol.up.messagebody.Tjsatl_2017_0900_MessageBody;
import com.legaoyi.protocol.util.ByteUtils;

/**
 * @author <a href="mailto:shengbo.gao@gmail.com;78772895@qq.com">gaoshengbo</a>
 * @version 1.0.0
 * @since 2019-05-20
 */
@Component(MessageBodyDecoder.MESSAGE_BODY_DECODER_BEAN_PREFIX + "0900_tjsatl" + MessageBodyDecoder.MESSAGE_BODY_DECODER_BEAN_SUFFIX)
public class Tjsatl_2017_0900_MessageBodyDecoder implements MessageBodyDecoder {

    @Override
    public MessageBody decode(byte[] bytes) throws IllegalMessageException {
        Tjsatl_2017_0900_MessageBody messageBody = new Tjsatl_2017_0900_MessageBody();
        try {
            int offset = 0;
            messageBody.setType(ByteUtils.byte2int(bytes[offset++]));

            int count = ByteUtils.byte2int(bytes[offset++]);

            List<Map<String, Object>> messageList = new ArrayList<Map<String, Object>>();

            Map<String, Object> message;
            for (int i = 0; i < count; i++) {
                message = new HashMap<String, Object>();
                int deviceId = ByteUtils.byte2int(bytes[offset++]);
                message.put("deviceId", deviceId);

                int length = ByteUtils.byte2int(bytes[offset++]);
                if (messageBody.getType() == 0xf7) {
                    message.put("state", ByteUtils.byte2int(bytes[offset++]));

                    byte[] arr = new byte[4];
                    System.arraycopy(bytes, offset, arr, 0, arr.length);
                    message.put("alarm", ByteUtils.dword2long(arr));
                    offset += arr.length;
                } else if (messageBody.getType() == 0xf8) {
                    length = ByteUtils.byte2int(bytes[offset++]);

                    byte[] arr = new byte[length];
                    System.arraycopy(bytes, offset, arr, 0, arr.length);
                    message.put("enterpriseName", ByteUtils.bytes2ascii(arr));
                    offset += arr.length;

                    length = ByteUtils.byte2int(bytes[offset++]);

                    arr = new byte[length];
                    System.arraycopy(bytes, offset, arr, 0, arr.length);
                    message.put("productType", ByteUtils.bytes2ascii(arr));
                    offset += arr.length;

                    length = ByteUtils.byte2int(bytes[offset++]);

                    arr = new byte[length];
                    System.arraycopy(bytes, offset, arr, 0, arr.length);
                    message.put("hardwareVersion", ByteUtils.bytes2ascii(arr));
                    offset += arr.length;

                    length = ByteUtils.byte2int(bytes[offset++]);

                    arr = new byte[length];
                    System.arraycopy(bytes, offset, arr, 0, arr.length);
                    message.put("softwareVersion", ByteUtils.bytes2ascii(arr));
                    offset += arr.length;

                    length = ByteUtils.byte2int(bytes[offset++]);

                    arr = new byte[length];
                    System.arraycopy(bytes, offset, arr, 0, arr.length);
                    message.put("deviceId", ByteUtils.bytes2ascii(arr));
                    offset += arr.length;

                    length = ByteUtils.byte2int(bytes[offset++]);

                    arr = new byte[length];
                    System.arraycopy(bytes, offset, arr, 0, arr.length);
                    message.put("clientCode", ByteUtils.bytes2ascii(arr));
                    offset += arr.length;
                } else {
                    offset += length;
                    continue;
                }

                messageList.add(message);
            }
            messageBody.setMessageList(messageList);
            return messageBody;
        } catch (Exception e) {
            throw new IllegalMessageException(e);
        }
    }
}
