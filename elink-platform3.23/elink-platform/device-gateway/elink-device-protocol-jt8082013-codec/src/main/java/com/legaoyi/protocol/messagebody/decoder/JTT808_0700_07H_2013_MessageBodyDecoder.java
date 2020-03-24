package com.legaoyi.protocol.messagebody.decoder;

import org.springframework.stereotype.Component;

import com.legaoyi.protocol.exception.IllegalMessageException;
import com.legaoyi.protocol.message.MessageBody;
import com.legaoyi.protocol.message.decoder.MessageBodyDecoder;
import com.legaoyi.protocol.messagebody.decoder.JTT808_0700_2013_MessageBodyDecoder;
import com.legaoyi.protocol.up.messagebody.JTT808_0700_07H_2013_MessageBody;
import com.legaoyi.protocol.util.ByteUtils;
import com.legaoyi.protocol.util.DateUtils;

/**
 * @author <a href="mailto:shengbo.gao@gmail.com;78772895@qq.com">gaoshengbo</a>
 * @version 1.0.0
 * @since 2015-01-30
 */
@Component(MessageBodyDecoder.MESSAGE_BODY_DECODER_BEAN_PREFIX+"0700_07H_2013"+MessageBodyDecoder.MESSAGE_BODY_DECODER_BEAN_SUFFIX)
public class JTT808_0700_07H_2013_MessageBodyDecoder extends JTT808_0700_2013_MessageBodyDecoder {

    @Override
    public MessageBody decode(byte[] messageBody) throws IllegalMessageException {
        JTT808_0700_07H_2013_MessageBody message = new JTT808_0700_07H_2013_MessageBody();
        try {
            int offset = 9;
            
            byte[] arr = new byte[7];
            System.arraycopy(messageBody, offset, arr, 0, arr.length);
            message.setCccAuthcode(ByteUtils.bytes2ascii(arr));
            offset += arr.length;

            arr = new byte[16];
            System.arraycopy(messageBody, offset, arr, 0, arr.length);
            message.setProductModel(ByteUtils.bytes2ascii(arr));
            offset += arr.length;

            arr = new byte[3];
            System.arraycopy(messageBody, offset, arr, 0, arr.length);
            message.setMfgdate(DateUtils.bcd2dateTime(ByteUtils.bytes2bcd(arr)));
            offset += arr.length;

            arr = new byte[4];
            System.arraycopy(messageBody, offset, arr, 0, arr.length);
            message.setBatchNo(String.valueOf(ByteUtils.bytesToInt(arr)));
            return message;
        } catch (Exception e) {
            throw new IllegalMessageException(e);
        }
    }
}
