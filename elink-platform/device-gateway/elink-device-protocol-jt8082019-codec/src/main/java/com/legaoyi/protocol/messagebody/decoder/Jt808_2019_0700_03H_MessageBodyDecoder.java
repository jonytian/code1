package com.legaoyi.protocol.messagebody.decoder;

import org.springframework.stereotype.Component;

import com.legaoyi.protocol.exception.IllegalMessageException;
import com.legaoyi.protocol.message.MessageBody;
import com.legaoyi.protocol.message.decoder.MessageBodyDecoder;
import com.legaoyi.protocol.messagebody.decoder.Jt808_2019_0700_MessageBodyDecoder;
import com.legaoyi.protocol.upstream.messagebody.Jt808_2019_0700_03H_MessageBody;
import com.legaoyi.protocol.util.ArithUtil;
import com.legaoyi.protocol.util.ByteUtils;
import com.legaoyi.protocol.util.DateUtils;

/**
 * @author <a href="mailto:shengbo.gao@gmail.com;78772895@qq.com">gaoshengbo</a>
 * @version 1.0.0
 * @since 2019-05-20
 */
@Component(MessageBodyDecoder.MESSAGE_BODY_DECODER_BEAN_PREFIX + "0700_03H_2019" + MessageBodyDecoder.MESSAGE_BODY_DECODER_BEAN_SUFFIX)
public class Jt808_2019_0700_03H_MessageBodyDecoder extends Jt808_2019_0700_MessageBodyDecoder {

    @Override
    public MessageBody decode(byte[] bytes) throws IllegalMessageException {
        Jt808_2019_0700_03H_MessageBody messageBody = new Jt808_2019_0700_03H_MessageBody();
        try {

            int offset = 9;

            byte[] arr = new byte[6];
            System.arraycopy(bytes, offset, arr, 0, arr.length);
            messageBody.setRealTime(DateUtils.bcd2dateTime(ByteUtils.bytes2bcd(arr)));
            offset += arr.length;

            arr = new byte[6];
            System.arraycopy(bytes, offset, arr, 0, arr.length);
            messageBody.setInstallTime(DateUtils.bcd2dateTime(ByteUtils.bytes2bcd(arr)));
            offset += arr.length;

            arr = new byte[4];
            System.arraycopy(bytes, offset, arr, 0, arr.length);
            messageBody.setInitialMileage(ArithUtil.div(Integer.parseInt(ByteUtils.bytes2bcd(arr)), 10.0D));
            offset += arr.length;

            arr = new byte[4];
            System.arraycopy(bytes, offset, arr, 0, arr.length);
            messageBody.setMileage(ArithUtil.div(Integer.parseInt(ByteUtils.bytes2bcd(arr)), 10.0D));

            return messageBody;
        } catch (Exception e) {
            throw new IllegalMessageException(e);
        }
    }
}
