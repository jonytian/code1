package com.legaoyi.protocol.messagebody.decoder;

import org.springframework.stereotype.Component;

import com.legaoyi.protocol.exception.IllegalMessageException;
import com.legaoyi.protocol.message.MessageBody;
import com.legaoyi.protocol.message.decoder.MessageBodyDecoder;
import com.legaoyi.protocol.up.messagebody.JTT1078_1005_MessageBody;
import com.legaoyi.protocol.util.ByteUtils;
import com.legaoyi.protocol.util.DateUtils;

/**
 * @author <a href="mailto:shengbo.gao@gmail.com;78772895@qq.com">gaoshengbo</a>
 * @version 1.0.0
 * @since 2018-04-09
 */
@Component(MessageBodyDecoder.MESSAGE_BODY_DECODER_BEAN_PREFIX + "1005_2016" + MessageBodyDecoder.MESSAGE_BODY_DECODER_BEAN_SUFFIX)
public class JTT1078_1005_MessageBodyDecoder implements MessageBodyDecoder {

    @Override
    public MessageBody decode(byte[] messageBody) throws IllegalMessageException {
        JTT1078_1005_MessageBody message = new JTT1078_1005_MessageBody();
        try {
            int offset = 0;
            byte[] arr = new byte[6];
            System.arraycopy(messageBody, offset, arr, 0, arr.length);
            String dateStr = ByteUtils.bytes2bcd(arr);
            if ("000000000000".equals(dateStr)) {
                dateStr = "000101000000";
            }
            message.setStartTime(DateUtils.bcd2dateTime(dateStr));

            offset += arr.length;
            arr = new byte[6];
            System.arraycopy(messageBody, offset, arr, 0, arr.length);
            dateStr = ByteUtils.bytes2bcd(arr);
            if ("000000000000".equals(dateStr)) {
                dateStr = "000101000000";
            }
            message.setEndTime(DateUtils.bcd2dateTime(dateStr));

            offset += arr.length;
            arr = new byte[2];
            System.arraycopy(messageBody, offset, arr, 0, arr.length);
            message.setInCar(ByteUtils.word2int(arr));

            offset += arr.length;
            arr = new byte[2];
            System.arraycopy(messageBody, offset, arr, 0, arr.length);
            message.setOffCar(ByteUtils.word2int(arr));
            return message;
        } catch (Exception e) {
            throw new IllegalMessageException(e);
        }
    }
}
