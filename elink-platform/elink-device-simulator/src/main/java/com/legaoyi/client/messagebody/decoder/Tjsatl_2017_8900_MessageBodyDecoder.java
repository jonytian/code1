package com.legaoyi.client.messagebody.decoder;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;
import com.legaoyi.client.down.messagebody.Tjsatl_2017_8900_MessageBody;
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
@Component(MessageBodyDecoder.MESSAGE_BODY_DECODER_BEAN_PREFIX + "8900_tjsatl" + MessageBodyDecoder.MESSAGE_BODY_DECODER_BEAN_SUFFIX)
public class Tjsatl_2017_8900_MessageBodyDecoder implements MessageBodyDecoder {

    @Override
    public MessageBody decode(byte[] messageBody) throws IllegalMessageException {
        Tjsatl_2017_8900_MessageBody body = new Tjsatl_2017_8900_MessageBody();
        try {
            int offset = 0;
            body.setType(ByteUtils.byte2int(messageBody[offset++]));
            int count = ByteUtils.byte2int(messageBody[offset++]);

            List<Integer> list = new ArrayList<Integer>();
            for (int i = 0; i < count; i++) {
                list.add(ByteUtils.byte2int(messageBody[offset++]));
            }
            body.setIdList(list);
        } catch (Exception e) {
            throw new IllegalMessageException(e);
        }
        return body;
    }
}
