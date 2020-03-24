package com.legaoyi.client.messagebody.decoder;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.legaoyi.client.down.messagebody.Tjsatl_2017_8103_MessageBody;
import com.legaoyi.protocol.exception.IllegalMessageException;
import com.legaoyi.protocol.message.MessageBody;
import com.legaoyi.protocol.message.decoder.MessageBodyDecoder;
import com.legaoyi.protocol.util.ByteUtils;

/**
 * 
 * @author <a href="mailto:shengbo.gao@gmail.com;78772895@qq.com">gaoshengbo</a>
 * @version 1.0.0
 * @since 2018-08-07
 */
@Component(MessageBodyDecoder.MESSAGE_BODY_DECODER_BEAN_PREFIX + "8103_tjsatl" + MessageBodyDecoder.MESSAGE_BODY_DECODER_BEAN_SUFFIX)
public class Tjsatl_2017_8103_MessageBodyDecoder implements MessageBodyDecoder {

    @Override
    public MessageBody decode(byte[] messageBody) throws IllegalMessageException {
        Tjsatl_2017_8103_MessageBody body = new Tjsatl_2017_8103_MessageBody();
        try {
            int offset = 0;
            int count = ByteUtils.byte2int(messageBody[offset++]);

            Map<String, String> paramList = new HashMap<String, String>();
            for (int i = 0; i < count; i++) {
                byte[] arr = new byte[4];
                System.arraycopy(messageBody, offset, arr, 0, arr.length);
                String id = ByteUtils.bytes2hex(arr);
                offset += arr.length;

                int length = ByteUtils.byte2int(messageBody[offset++]);

                arr = new byte[length];
                System.arraycopy(messageBody, offset, arr, 0, arr.length);
                offset += arr.length;

                paramList.put(id, ByteUtils.bytes2hex(arr));
            }
            body.setParamList(paramList);
        } catch (Exception e) {
            throw new IllegalMessageException(e);
        }
        return body;
    }
}
