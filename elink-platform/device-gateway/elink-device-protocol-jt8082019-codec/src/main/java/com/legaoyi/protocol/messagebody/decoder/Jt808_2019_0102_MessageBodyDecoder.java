package com.legaoyi.protocol.messagebody.decoder;

import org.springframework.stereotype.Component;

import com.legaoyi.protocol.exception.IllegalMessageException;
import com.legaoyi.protocol.message.MessageBody;
import com.legaoyi.protocol.message.decoder.MessageBodyDecoder;
import com.legaoyi.protocol.upstream.messagebody.Jt808_2019_0102_MessageBody;
import com.legaoyi.protocol.util.ByteUtils;

/**
 * @author <a href="mailto:shengbo.gao@gmail.com;78772895@qq.com">gaoshengbo</a>
 * @version 1.0.0
 * @since 2019-05-20
 */
@Component(MessageBodyDecoder.MESSAGE_BODY_DECODER_BEAN_PREFIX + "0102_2019" + MessageBodyDecoder.MESSAGE_BODY_DECODER_BEAN_SUFFIX)
public class Jt808_2019_0102_MessageBodyDecoder implements MessageBodyDecoder {

    @Override
    public MessageBody decode(byte[] bytes) throws IllegalMessageException {

        Jt808_2019_0102_MessageBody messageBody = new Jt808_2019_0102_MessageBody();
        try {
            int offset = 0;

            int length = ByteUtils.byte2int(bytes[offset]);
            offset++;

            byte[] arr = new byte[length];
            System.arraycopy(bytes, offset, arr, 0, arr.length);
            messageBody.setAuthCode(ByteUtils.bytes2gbk(arr));
            offset += arr.length;

            arr = new byte[15];
            System.arraycopy(bytes, offset, arr, 0, arr.length);
            messageBody.setImei(ByteUtils.bytes2gbk(arr));
            offset += arr.length;

            arr = new byte[20];
            System.arraycopy(bytes, offset, arr, 0, arr.length);
            messageBody.setVersion(ByteUtils.bytes2gbk(arr));

            return messageBody;
        } catch (Exception e) {
            throw new IllegalMessageException(e);
        }
    }
}
