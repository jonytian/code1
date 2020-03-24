package com.legaoyi.protocol.messagebody.decoder;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.legaoyi.protocol.exception.IllegalMessageException;
import com.legaoyi.protocol.message.MessageBody;
import com.legaoyi.protocol.message.decoder.MessageBodyDecoder;
import com.legaoyi.protocol.up.messagebody.JTT808_0700_05H_MessageBody;
import com.legaoyi.protocol.util.ByteUtils;
import com.legaoyi.protocol.util.DateUtils;

/**
 * @author <a href="mailto:shengbo.gao@gmail.com;78772895@qq.com">gaoshengbo</a>
 * @version 1.0.0
 * @since 2015-01-30
 */
@Component(MessageBodyDecoder.MESSAGE_BODY_DECODER_BEAN_PREFIX + "0700_05H_2011" + MessageBodyDecoder.MESSAGE_BODY_DECODER_BEAN_SUFFIX)
public class JTT808_0700_05H_MessageBodyDecoder extends JTT808_0700_MessageBodyDecoder {

    @Override
    public MessageBody decode(byte[] messageBody) throws IllegalMessageException {
        JTT808_0700_05H_MessageBody message = new JTT808_0700_05H_MessageBody();
        try {
            int offset = 9;

            byte[] arr = new byte[5];
            System.arraycopy(messageBody, offset, arr, 0, arr.length);
            message.setStartTime(DateUtils.bcd2dateTime(ByteUtils.bytes2bcd(arr)));
            offset += arr.length;

            List<Integer> speedList = new ArrayList<Integer>();
            int dataOffset = messageBody.length - 1;
            while (offset < dataOffset) {
                speedList.add(ByteUtils.byte2int(messageBody[offset++]));
            }
            message.setSpeedList(speedList);

            return message;
        } catch (Exception e) {
            throw new IllegalMessageException(e);
        }
    }
}
