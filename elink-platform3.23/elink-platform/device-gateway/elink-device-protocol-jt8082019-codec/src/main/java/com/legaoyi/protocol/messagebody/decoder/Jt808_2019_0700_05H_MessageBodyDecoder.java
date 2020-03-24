package com.legaoyi.protocol.messagebody.decoder;

import org.springframework.stereotype.Component;
import com.legaoyi.protocol.exception.IllegalMessageException;
import com.legaoyi.protocol.message.MessageBody;
import com.legaoyi.protocol.message.decoder.MessageBodyDecoder;
import com.legaoyi.protocol.messagebody.decoder.Jt808_2019_0700_MessageBodyDecoder;
import com.legaoyi.protocol.upstream.messagebody.Jt808_2019_0700_05H_MessageBody;
import com.legaoyi.protocol.util.ByteUtils;

/**
 * @author <a href="mailto:shengbo.gao@gmail.com;78772895@qq.com">gaoshengbo</a>
 * @version 1.0.0
 * @since 2019-05-20
 */
@Component(MessageBodyDecoder.MESSAGE_BODY_DECODER_BEAN_PREFIX + "0700_05H_2019" + MessageBodyDecoder.MESSAGE_BODY_DECODER_BEAN_SUFFIX)
public class Jt808_2019_0700_05H_MessageBodyDecoder extends Jt808_2019_0700_MessageBodyDecoder {

    @Override
    public MessageBody decode(byte[] bytes) throws IllegalMessageException {
        Jt808_2019_0700_05H_MessageBody messageBody = new Jt808_2019_0700_05H_MessageBody();
        try {

            int offset = 9;

            byte[] arr = new byte[17];
            System.arraycopy(bytes, offset, arr, 0, arr.length);
            messageBody.setVin(ByteUtils.bytes2ascii(arr));
            offset += arr.length;

            arr = new byte[2];
            System.arraycopy(bytes, offset, arr, 0, arr.length);
            String plateNo = ByteUtils.bytes2gb2312(arr);
            offset += arr.length;

            arr = new byte[7];
            System.arraycopy(bytes, offset, arr, 0, arr.length);
            plateNo += ByteUtils.bytes2ascii(arr);
            offset += arr.length;

            messageBody.setPlateNo(plateNo);
            offset += 3;

            arr = new byte[8];
            System.arraycopy(bytes, offset, arr, 0, arr.length);
            messageBody.setPlateType(ByteUtils.bytes2gb2312(arr));

            return messageBody;
        } catch (Exception e) {
            throw new IllegalMessageException(e);
        }
    }
}
