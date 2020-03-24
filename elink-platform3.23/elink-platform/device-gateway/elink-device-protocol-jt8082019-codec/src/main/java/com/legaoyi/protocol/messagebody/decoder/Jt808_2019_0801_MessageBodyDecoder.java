package com.legaoyi.protocol.messagebody.decoder;

import org.springframework.stereotype.Component;

import com.legaoyi.protocol.exception.IllegalMessageException;
import com.legaoyi.protocol.message.MessageBody;
import com.legaoyi.protocol.message.decoder.MessageBodyDecoder;
import com.legaoyi.protocol.upstream.messagebody.Jt808_2019_0200_MessageBody;
import com.legaoyi.protocol.upstream.messagebody.Jt808_2019_0801_MessageBody;

/**
 * @author <a href="mailto:shengbo.gao@gmail.com;78772895@qq.com">gaoshengbo</a>
 * @version 1.0.0
 * @since 2019-05-20
 */
@Component(MessageBodyDecoder.MESSAGE_BODY_DECODER_BEAN_PREFIX + "0801_2019" + MessageBodyDecoder.MESSAGE_BODY_DECODER_BEAN_SUFFIX)
public class Jt808_2019_0801_MessageBodyDecoder extends Jt808_2019_0800_MessageBodyDecoder {

    @Override
    public MessageBody decode(byte[] bytes) throws IllegalMessageException {
        Jt808_2019_0801_MessageBody messageBody = new Jt808_2019_0801_MessageBody();
        try {
            super.copy(super.decode(bytes), messageBody);

            int offset = 8;

            byte[] arr = new byte[28];
            System.arraycopy(bytes, offset, arr, 0, arr.length);
            Jt808_2019_0200_MessageBody gpsInfo = (Jt808_2019_0200_MessageBody) new Jt808_2019_0200_MessageBodyDecoder().decode(arr);
            messageBody.setGpsInfo(gpsInfo);
            offset += arr.length;

            arr = new byte[bytes.length - offset];
            System.arraycopy(bytes, offset, arr, 0, arr.length);
            messageBody.setFileData(arr);

            return messageBody;
        } catch (Exception e) {
            throw new IllegalMessageException(e);
        }
    }
}
