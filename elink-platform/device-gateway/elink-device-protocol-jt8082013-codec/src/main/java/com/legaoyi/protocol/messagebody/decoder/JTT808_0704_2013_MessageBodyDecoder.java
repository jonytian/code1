package com.legaoyi.protocol.messagebody.decoder;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.legaoyi.protocol.exception.IllegalMessageException;
import com.legaoyi.protocol.message.MessageBody;
import com.legaoyi.protocol.message.decoder.MessageBodyDecoder;
import com.legaoyi.protocol.messagebody.decoder.JTT808_0200_MessageBodyDecoder;
import com.legaoyi.protocol.up.messagebody.JTT808_0200_MessageBody;
import com.legaoyi.protocol.up.messagebody.JTT808_0704_2013_MessageBody;
import com.legaoyi.protocol.util.ByteUtils;

/**
 * @author <a href="mailto:shengbo.gao@gmail.com;78772895@qq.com">gaoshengbo</a>
 * @version 1.0.0
 * @since 2015-01-30
 */
@Component(MessageBodyDecoder.MESSAGE_BODY_DECODER_BEAN_PREFIX+"0704_2013"+MessageBodyDecoder.MESSAGE_BODY_DECODER_BEAN_SUFFIX)
public class JTT808_0704_2013_MessageBodyDecoder implements MessageBodyDecoder {

    @Override
    public MessageBody decode(byte[] messageBody) throws IllegalMessageException {
        JTT808_0704_2013_MessageBody message = new JTT808_0704_2013_MessageBody();
        try {
            int offset = 0;
            byte[] arr = new byte[2];
            System.arraycopy(messageBody, offset, arr, 0, arr.length);
            int count = ByteUtils.bytesToInt(arr);
            offset += arr.length;
            message.setType(ByteUtils.byte2int(messageBody[offset]));
            offset++;

            List<JTT808_0200_MessageBody> gpsList = new ArrayList<JTT808_0200_MessageBody>();
            for (int i = 0; i < count; i++) {
                arr = new byte[2];
                System.arraycopy(messageBody, offset, arr, 0, arr.length);
                int len = ByteUtils.bytesToInt(arr);
                offset += arr.length;
                arr = new byte[len];
				System.arraycopy(messageBody, offset, arr, 0, arr.length);
                JTT808_0200_MessageBody body =
                        (JTT808_0200_MessageBody) new JTT808_0200_MessageBodyDecoder().decode(arr);
                gpsList.add(body);
                offset += arr.length;
            }
            message.setGpsInfoList(gpsList);
            return message;
        } catch (Exception e) {
            throw new IllegalMessageException(e);
        }
    }
}
