package com.legaoyi.protocol.messagebody.decoder;

import org.springframework.stereotype.Component;
import com.legaoyi.protocol.exception.IllegalMessageException;
import com.legaoyi.protocol.message.MessageBody;
import com.legaoyi.protocol.message.decoder.MessageBodyDecoder;
import com.legaoyi.protocol.up.messagebody.JTT808_0100_MessageBody;
import com.legaoyi.protocol.util.ByteUtils;

/**
 * @author <a href="mailto:shengbo.gao@gmail.com;78772895@qq.com">gaoshengbo</a>
 * @version 1.0.0
 * @since 2015-01-30
 */
@Component(MessageBodyDecoder.MESSAGE_BODY_DECODER_BEAN_PREFIX+"0100_2011"+MessageBodyDecoder.MESSAGE_BODY_DECODER_BEAN_SUFFIX)
public class JTT808_0100_MessageBodyDecoder implements MessageBodyDecoder {

    @Override
    public MessageBody decode(byte[] messageBody) throws IllegalMessageException {
        JTT808_0100_MessageBody message = new JTT808_0100_MessageBody();
        try {
            int offset = 0;
            byte[] arr = new byte[2];
            System.arraycopy(messageBody, offset, arr, 0, arr.length);
            message.setProvinceId(ByteUtils.word2int(arr));

            offset += arr.length;
            arr = new byte[2];
            System.arraycopy(messageBody, offset, arr, 0, arr.length);
            message.setCityId(ByteUtils.word2int(arr));

            offset += arr.length;
            arr = new byte[5];
            System.arraycopy(messageBody, offset, arr, 0, arr.length);
            message.setMfrsId(ByteUtils.bytes2gbk(arr));

            offset += arr.length;
            arr = new byte[20];
            System.arraycopy(messageBody, offset, arr, 0, arr.length);
            message.setTerminalType(ByteUtils.bytes2gbk(arr));

            offset += arr.length;
            arr = new byte[7];
            System.arraycopy(messageBody, offset, arr, 0, arr.length);
            message.setTerminalId(ByteUtils.bytes2gbk(arr));

            offset += arr.length;
            arr = new byte[1];
            System.arraycopy(messageBody, offset, arr, 0, arr.length);
            message.setVinColor(ByteUtils.byte2int(arr[0]));

            offset += arr.length;
            arr = new byte[messageBody.length - offset];
            System.arraycopy(messageBody, offset, arr, 0, arr.length);
            message.setVin(ByteUtils.bytes2gbk(arr));
            return message;
        } catch (Exception e) {
            throw new IllegalMessageException(e);
        }
    }
}
