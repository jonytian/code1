package com.legaoyi.protocol.messagebody.decoder;

import org.springframework.stereotype.Component;

import com.legaoyi.protocol.exception.IllegalMessageException;
import com.legaoyi.protocol.message.MessageBody;
import com.legaoyi.protocol.message.decoder.MessageBodyDecoder;
import com.legaoyi.protocol.up.messagebody.JTT808_0101_MessageBody;
import com.legaoyi.protocol.util.ByteUtils;

/**
 * @author <a href="mailto:shengbo.gao@gmail.com;78772895@qq.com">gaoshengbo</a>
 * @version 1.0.0
 * @since 2015-01-30
 */
@Component(MessageBodyDecoder.MESSAGE_BODY_DECODER_BEAN_PREFIX+"0101_2011"+MessageBodyDecoder.MESSAGE_BODY_DECODER_BEAN_SUFFIX)
public class JTT808_0101_MessageBodyDecoder implements MessageBodyDecoder {

    @Override
    public MessageBody decode(byte[] messageBody) throws IllegalMessageException {
        JTT808_0101_MessageBody message = new JTT808_0101_MessageBody();
        try {
            int offset = 0;
            byte[] arr = new byte[2];
            System.arraycopy(messageBody, offset, arr, 0, arr.length);
            message.setMessageId(ByteUtils.bytes2hex(arr));
            offset += arr.length;
            System.arraycopy(messageBody, offset, arr, 0, arr.length);
            message.setMessageSeq(ByteUtils.word2int(arr));
            offset += arr.length;
            arr = new byte[1];
            System.arraycopy(messageBody, offset, arr, 0, arr.length);
            message.setResult(ByteUtils.bytesToInt(arr));
            return message;
        } catch (Exception e) {
            throw new IllegalMessageException(e);
        }
    }
}
