package com.legaoyi.protocol.messagebody.decoder;

import org.springframework.stereotype.Component;

import com.legaoyi.protocol.exception.IllegalMessageException;
import com.legaoyi.protocol.message.MessageBody;
import com.legaoyi.protocol.message.decoder.MessageBodyDecoder;
import com.legaoyi.protocol.up.messagebody.JTT808_0900_MessageBody;
import com.legaoyi.protocol.util.ByteUtils;

/**
 * @author <a href="mailto:shengbo.gao@gmail.com;78772895@qq.com">gaoshengbo</a>
 * @version 1.0.0
 * @since 2015-01-30
 */
@Component(MessageBodyDecoder.MESSAGE_BODY_DECODER_BEAN_PREFIX + "0900_2013" + MessageBodyDecoder.MESSAGE_BODY_DECODER_BEAN_SUFFIX)
public class JTT808_0900_2013_MessageBodyDecoder implements MessageBodyDecoder {

    @Override
    public MessageBody decode(byte[] messageBody) throws IllegalMessageException {
        JTT808_0900_MessageBody message = new JTT808_0900_MessageBody();
        try {
            int offset = 0;
            message.setMessageType(ByteUtils.byte2hex(messageBody[offset]));
            offset++;

            byte[] arr = new byte[messageBody.length - offset];
            System.arraycopy(messageBody, offset, arr, 0, arr.length);
            message.setMessage(ByteUtils.bytes2hex(arr));

            return message;
        } catch (Exception e) {
            throw new IllegalMessageException(e);
        }
    }
}
