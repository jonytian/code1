package com.legaoyi.protocol.messagebody.decoder;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.legaoyi.protocol.exception.IllegalMessageException;
import com.legaoyi.protocol.message.MessageBody;
import com.legaoyi.protocol.message.decoder.MessageBodyDecoder;
import com.legaoyi.protocol.up.messagebody.Tjsatl_2017_0200_MessageBody;
import com.legaoyi.protocol.up.messagebody.Tjsatl_2017_0704_MessageBody;
import com.legaoyi.protocol.util.ByteUtils;

/**
 * @author <a href="mailto:shengbo.gao@gmail.com;78772895@qq.com">gaoshengbo</a>
 * @version 1.0.0
 * @since 2019-05-20
 */
@Component(MessageBodyDecoder.MESSAGE_BODY_DECODER_BEAN_PREFIX + "0704_tjsatl" + MessageBodyDecoder.MESSAGE_BODY_DECODER_BEAN_SUFFIX)
public class Tjsatl_2017_0704_MessageBodyDecoder implements MessageBodyDecoder {

    @Override
    public MessageBody decode(byte[] bytes) throws IllegalMessageException {
        Tjsatl_2017_0704_MessageBody messageBody = new Tjsatl_2017_0704_MessageBody();
        try {
            int offset = 0;

            byte[] arr = new byte[2];
            System.arraycopy(bytes, offset, arr, 0, arr.length);
            int count = ByteUtils.word2int(arr);
            offset += arr.length;

            messageBody.setType(ByteUtils.byte2int(bytes[offset]));
            offset++;

            List<Tjsatl_2017_0200_MessageBody> gpsList = new ArrayList<Tjsatl_2017_0200_MessageBody>();
            for (int i = 0; i < count; i++) {
                arr = new byte[2];
                System.arraycopy(bytes, offset, arr, 0, arr.length);
                int len = ByteUtils.word2int(arr);
                offset += arr.length;

                arr = new byte[len];
                System.arraycopy(bytes, offset, arr, 0, arr.length);
                Tjsatl_2017_0200_MessageBody body = (Tjsatl_2017_0200_MessageBody) new Tjsatl_2017_0200_MessageBodyDecoder().decode(arr);
                gpsList.add(body);
                offset += arr.length;
            }
            messageBody.setGpsInfoList(gpsList);
            return messageBody;
        } catch (Exception e) {
            throw new IllegalMessageException(e);
        }
    }
}
