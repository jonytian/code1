package com.legaoyi.client.messagebody.decoder;

import org.springframework.stereotype.Component;

import com.legaoyi.client.down.messagebody.Jt808_2011_8001_MessageBody;
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
@Component(MessageBodyDecoder.MESSAGE_BODY_DECODER_BEAN_PREFIX + "8001_2011" + MessageBodyDecoder.MESSAGE_BODY_DECODER_BEAN_SUFFIX)
public class JTT808_8001_2011_MessageBodyDecoder implements MessageBodyDecoder {

    @Override
    public MessageBody decode(byte[] messageBody) throws IllegalMessageException {
        Jt808_2011_8001_MessageBody jttMessageBody = new Jt808_2011_8001_MessageBody();
        try {
            int offset = 0;

            byte[] arr = new byte[2];
            System.arraycopy(messageBody, offset, arr, 0, arr.length);
            jttMessageBody.setMessageSeq(ByteUtils.word2int(arr));
            offset += arr.length;

            arr = new byte[2];
            System.arraycopy(messageBody, offset, arr, 0, arr.length);
            jttMessageBody.setMessageId(ByteUtils.bytes2hex(arr));
            offset += arr.length;

            jttMessageBody.setResult(ByteUtils.byte2int(messageBody[offset]));
        } catch (Exception e) {
            throw new IllegalMessageException(e);
        }
        return jttMessageBody;
    }
}
