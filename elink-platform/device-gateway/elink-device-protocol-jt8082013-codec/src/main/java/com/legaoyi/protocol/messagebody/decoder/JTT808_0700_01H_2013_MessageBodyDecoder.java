package com.legaoyi.protocol.messagebody.decoder;

import org.springframework.stereotype.Component;

import com.legaoyi.protocol.exception.IllegalMessageException;
import com.legaoyi.protocol.message.MessageBody;
import com.legaoyi.protocol.message.decoder.MessageBodyDecoder;
import com.legaoyi.protocol.messagebody.decoder.JTT808_0700_2013_MessageBodyDecoder;
import com.legaoyi.protocol.up.messagebody.JTT808_0700_01H_2013_MessageBody;
import com.legaoyi.protocol.util.ByteUtils;


/**
 * @author <a href="mailto:shengbo.gao@gmail.com;78772895@qq.com">gaoshengbo</a>
 * @version 1.0.0
 * @since 2015-01-30
 */

@Component(MessageBodyDecoder.MESSAGE_BODY_DECODER_BEAN_PREFIX+"0700_01H_2013"+MessageBodyDecoder.MESSAGE_BODY_DECODER_BEAN_SUFFIX)
public class JTT808_0700_01H_2013_MessageBodyDecoder extends JTT808_0700_2013_MessageBodyDecoder {

    @Override
    public MessageBody decode(byte[] messageBody) throws IllegalMessageException {
        JTT808_0700_01H_2013_MessageBody message = new JTT808_0700_01H_2013_MessageBody();
        try {
            int offset = 9;
            
            byte[] arr = new byte[18];
            System.arraycopy(messageBody, offset, arr, 0, arr.length);
            message.setDriverLicense(ByteUtils.bytes2ascii(arr));
            
            return message;
        } catch (Exception e) {
            throw new IllegalMessageException(e);
        }
    }
}
