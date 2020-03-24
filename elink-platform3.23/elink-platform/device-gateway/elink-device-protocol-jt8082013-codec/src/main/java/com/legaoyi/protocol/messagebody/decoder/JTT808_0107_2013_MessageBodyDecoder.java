package com.legaoyi.protocol.messagebody.decoder;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import com.legaoyi.protocol.exception.IllegalMessageException;
import com.legaoyi.protocol.message.MessageBody;
import com.legaoyi.protocol.message.decoder.MessageBodyDecoder;
import com.legaoyi.protocol.up.messagebody.JTT808_0107_2013_MessageBody;
import com.legaoyi.protocol.util.ByteUtils;

/**
 * @author <a href="mailto:shengbo.gao@gmail.com;78772895@qq.com">gaoshengbo</a>
 * @version 1.0.0
 * @since 2015-01-30
 */
@Component(MessageBodyDecoder.MESSAGE_BODY_DECODER_BEAN_PREFIX+"0107_2013"+MessageBodyDecoder.MESSAGE_BODY_DECODER_BEAN_SUFFIX)
public class JTT808_0107_2013_MessageBodyDecoder implements MessageBodyDecoder {

    @Override
    public MessageBody decode(byte[] messageBody) throws IllegalMessageException {
        JTT808_0107_2013_MessageBody message = new JTT808_0107_2013_MessageBody();
        try {
            int offset = 0;
            byte[] arr = new byte[2];
            System.arraycopy(messageBody, offset, arr, 0, arr.length);
            message.setTerminalType(StringUtils.reverse(ByteUtils.word2bin(arr)));
            offset += arr.length;

            arr = new byte[5];
            System.arraycopy(messageBody, offset, arr, 0, arr.length);
            message.setMfrsId(ByteUtils.bytes2gbk(arr));
            offset += arr.length;

            arr = new byte[20];
            System.arraycopy(messageBody, offset, arr, 0, arr.length);
            message.setTerminalModel(ByteUtils.bytes2gbk(arr));
            offset += arr.length;

            arr = new byte[7];
            System.arraycopy(messageBody, offset, arr, 0, arr.length);
            message.setTerminalId(ByteUtils.bytes2gbk(arr));
            offset += arr.length;

            arr = new byte[10];
            System.arraycopy(messageBody, offset, arr, 0, arr.length);
            message.setTerminalId(ByteUtils.bytes2bcd(arr));
            offset += arr.length;

            int len = ByteUtils.byte2int(messageBody[offset]);
            offset++;

            arr = new byte[len];
            System.arraycopy(messageBody, offset, arr, 0, arr.length);
            message.setHardwareVersion(ByteUtils.bytes2gbk(arr));
            offset += arr.length;

            len = ByteUtils.byte2int(messageBody[offset]);
            offset++;

            arr = new byte[len];
            System.arraycopy(messageBody, offset, arr, 0, arr.length);
            message.setFirmwareVersion(ByteUtils.bytes2gbk(arr));
            offset += arr.length;

            message.setGnssAttribute(StringUtils.reverse(ByteUtils.byte2bin(messageBody[offset])));
            offset++;
            message.setCmAttribute(StringUtils.reverse(ByteUtils.byte2bin(messageBody[offset])));
            // offset++;
            return message;
        } catch (Exception e) {
            throw new IllegalMessageException(e);
        }
    }
}
