package com.legaoyi.protocol.messagebody.decoder;

import org.springframework.stereotype.Component;

import com.legaoyi.protocol.exception.IllegalMessageException;
import com.legaoyi.protocol.message.MessageBody;
import com.legaoyi.protocol.message.decoder.MessageBodyDecoder;
import com.legaoyi.protocol.up.messagebody.JTT808_0200_MessageBody;
import com.legaoyi.protocol.up.messagebody.JTT808_0801_MessageBody;

/**
 * @author <a href="mailto:shengbo.gao@gmail.com;78772895@qq.com">gaoshengbo</a>
 * @version 1.0.0
 * @since 2015-01-30
 */
@Component(MessageBodyDecoder.MESSAGE_BODY_DECODER_BEAN_PREFIX + "0801_2011" + MessageBodyDecoder.MESSAGE_BODY_DECODER_BEAN_SUFFIX)
public class JTT808_0801_MessageBodyDecoder extends JTT808_0800_MessageBodyDecoder {

    @Override
    public MessageBody decode(byte[] messageBody) throws IllegalMessageException {
        JTT808_0801_MessageBody message = new JTT808_0801_MessageBody();
        try {
            super.copy(super.decode(messageBody), message);
            int offset = 8;
            byte[] arr = new byte[28];
            System.arraycopy(messageBody, offset, arr, 0, arr.length);
            JTT808_0200_MessageBody gpsInfo = (JTT808_0200_MessageBody) new JTT808_0200_MessageBodyDecoder().decode(arr);
            message.setGpsInfo(gpsInfo);
            offset += arr.length;

            arr = new byte[messageBody.length - offset];
            System.arraycopy(messageBody, offset, arr, 0, arr.length);
            message.setMediaData(arr);
            return message;
        } catch (Exception e) {
            throw new IllegalMessageException(e);
        }
    }
}
