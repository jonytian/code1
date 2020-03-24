package com.legaoyi.protocol.messagebody.decoder;

import org.springframework.stereotype.Component;

import com.legaoyi.protocol.exception.IllegalMessageException;
import com.legaoyi.protocol.message.MessageBody;
import com.legaoyi.protocol.message.decoder.MessageBodyDecoder;
import com.legaoyi.protocol.upstream.messagebody.Jt808_2019_0100_MessageBody;
import com.legaoyi.protocol.util.ByteUtils;

/**
 * @author <a href="mailto:shengbo.gao@gmail.com;78772895@qq.com">gaoshengbo</a>
 * @version 1.0.0
 * @since 2019-05-20
 */
@Component(MessageBodyDecoder.MESSAGE_BODY_DECODER_BEAN_PREFIX + "0100_2019" + MessageBodyDecoder.MESSAGE_BODY_DECODER_BEAN_SUFFIX)
public class Jt808_2019_0100_MessageBodyDecoder implements MessageBodyDecoder {

    @Override
    public MessageBody decode(byte[] bytes) throws IllegalMessageException {
        Jt808_2019_0100_MessageBody messageBody = new Jt808_2019_0100_MessageBody();
        try {

            int offset = 0;

            byte[] arr = new byte[2];
            System.arraycopy(bytes, offset, arr, 0, arr.length);
            messageBody.setProvinceId(ByteUtils.word2int(arr));
            offset += arr.length;

            System.arraycopy(bytes, offset, arr, 0, arr.length);
            messageBody.setCityId(ByteUtils.word2int(arr));
            offset += arr.length;

            arr = new byte[11];
            System.arraycopy(bytes, offset, arr, 0, arr.length);
            messageBody.setMfrsId(ByteUtils.bytes2gbk(arr));
            offset += arr.length;

            arr = new byte[30];
            System.arraycopy(bytes, offset, arr, 0, arr.length);
            messageBody.setTerminalType(ByteUtils.bytes2gbk(arr));
            offset += arr.length;

            System.arraycopy(bytes, offset, arr, 0, arr.length);
            messageBody.setTerminalId(ByteUtils.bytes2gbk(arr));
            offset += arr.length;

            messageBody.setVinColor(ByteUtils.byte2int(bytes[offset]));
            offset++;

            arr = new byte[bytes.length - offset];
            System.arraycopy(bytes, offset, arr, 0, arr.length);
            messageBody.setVin(ByteUtils.bytes2gbk(arr));
            return messageBody;
        } catch (Exception e) {
            throw new IllegalMessageException(e);
        }
    }
}
