package com.legaoyi.protocol.messagebody.decoder;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import com.legaoyi.protocol.exception.IllegalMessageException;
import com.legaoyi.protocol.message.MessageBody;
import com.legaoyi.protocol.message.decoder.MessageBodyDecoder;
import com.legaoyi.protocol.upstream.messagebody.Jt808_2019_0107_MessageBody;
import com.legaoyi.protocol.util.ByteUtils;

/**
 * @author <a href="mailto:shengbo.gao@gmail.com;78772895@qq.com">gaoshengbo</a>
 * @version 1.0.0
 * @since 2019-05-20
 */
@Component(MessageBodyDecoder.MESSAGE_BODY_DECODER_BEAN_PREFIX + "0107_2019" + MessageBodyDecoder.MESSAGE_BODY_DECODER_BEAN_SUFFIX)
public class Jt808_2019_0107_MessageBodyDecoder implements MessageBodyDecoder {

    @Override
    public MessageBody decode(byte[] bytes) throws IllegalMessageException {
        Jt808_2019_0107_MessageBody messageBody = new Jt808_2019_0107_MessageBody();
        try {
            int offset = 0;

            byte[] arr = new byte[2];
            System.arraycopy(bytes, offset, arr, 0, arr.length);
            messageBody.setTerminalType(StringUtils.reverse(ByteUtils.word2bin(arr)));
            offset += arr.length;

            arr = new byte[5];
            System.arraycopy(bytes, offset, arr, 0, arr.length);
            messageBody.setMfrsId(ByteUtils.bytes2gbk(arr));
            offset += arr.length;

            arr = new byte[30];
            System.arraycopy(bytes, offset, arr, 0, arr.length);
            messageBody.setTerminalModel(ByteUtils.bytes2gbk(arr));
            offset += arr.length;

            arr = new byte[30];
            System.arraycopy(bytes, offset, arr, 0, arr.length);
            messageBody.setTerminalId(ByteUtils.bytes2gbk(arr));
            offset += arr.length;

            arr = new byte[10];
            System.arraycopy(bytes, offset, arr, 0, arr.length);
            messageBody.setTerminalId(ByteUtils.bytes2bcd(arr));
            offset += arr.length;

            int len = ByteUtils.byte2int(bytes[offset]);
            offset++;

            arr = new byte[len];
            System.arraycopy(bytes, offset, arr, 0, arr.length);
            messageBody.setHardwareVersion(ByteUtils.bytes2gbk(arr));
            offset += arr.length;

            len = ByteUtils.byte2int(bytes[offset]);
            offset++;

            arr = new byte[len];
            System.arraycopy(bytes, offset, arr, 0, arr.length);
            messageBody.setFirmwareVersion(ByteUtils.bytes2gbk(arr));
            offset += arr.length;

            messageBody.setGnssAttribute(StringUtils.reverse(ByteUtils.byte2bin(bytes[offset])));
            offset++;

            messageBody.setCmAttribute(StringUtils.reverse(ByteUtils.byte2bin(bytes[offset])));

            return messageBody;
        } catch (Exception e) {
            throw new IllegalMessageException(e);
        }
    }
}
