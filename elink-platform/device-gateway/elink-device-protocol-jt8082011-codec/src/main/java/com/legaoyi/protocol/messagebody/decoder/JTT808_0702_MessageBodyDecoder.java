package com.legaoyi.protocol.messagebody.decoder;

import org.springframework.stereotype.Component;
import com.legaoyi.protocol.exception.IllegalMessageException;
import com.legaoyi.protocol.message.MessageBody;
import com.legaoyi.protocol.message.decoder.MessageBodyDecoder;
import com.legaoyi.protocol.up.messagebody.JTT808_0702_MessageBody;
import com.legaoyi.protocol.util.ByteUtils;

/**
 * @author <a href="mailto:shengbo.gao@gmail.com;78772895@qq.com">gaoshengbo</a>
 * @version 1.0.0
 * @since 2015-01-30
 */
@Component(MessageBodyDecoder.MESSAGE_BODY_DECODER_BEAN_PREFIX + "0702_2011" + MessageBodyDecoder.MESSAGE_BODY_DECODER_BEAN_SUFFIX)
public class JTT808_0702_MessageBodyDecoder implements MessageBodyDecoder {

    @Override
    public MessageBody decode(byte[] messageBody) throws IllegalMessageException {
        JTT808_0702_MessageBody message = new JTT808_0702_MessageBody();
        try {
            int offset = 0;
            int len = ByteUtils.byte2int(messageBody[offset]);
            offset++;

            byte[] arr = new byte[len];
            System.arraycopy(messageBody, offset, arr, 0, arr.length);
            message.setDriverName(ByteUtils.bytes2gbk(arr));
            offset += arr.length;

            arr = new byte[20];
            System.arraycopy(messageBody, offset, arr, 0, arr.length);
            message.setIdCard(ByteUtils.bytes2gbk(arr));
            offset += arr.length;

            arr = new byte[40];
            System.arraycopy(messageBody, offset, arr, 0, arr.length);
            message.setQualification(ByteUtils.bytes2gbk(arr));
            offset += arr.length;

            len = ByteUtils.byte2int(messageBody[offset]);
            offset++;

            arr = new byte[len];
            System.arraycopy(messageBody, offset, arr, 0, arr.length);
            message.setCertifyauth(ByteUtils.bytes2gbk(arr));
            offset += arr.length;
            return message;
        } catch (Exception e) {
            throw new IllegalMessageException(e);
        }
    }
}
