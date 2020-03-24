package com.legaoyi.protocol.messagebody.decoder;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.legaoyi.protocol.exception.IllegalMessageException;
import com.legaoyi.protocol.message.MessageBody;
import com.legaoyi.protocol.message.decoder.MessageBodyDecoder;
import com.legaoyi.protocol.upstream.messagebody.Jt808_2019_0805_MessageBody;
import com.legaoyi.protocol.util.ByteUtils;

/**
 * @author <a href="mailto:shengbo.gao@gmail.com;78772895@qq.com">gaoshengbo</a>
 * @version 1.0.0
 * @since 2019-05-20
 */
@Component(MessageBodyDecoder.MESSAGE_BODY_DECODER_BEAN_PREFIX + "0805_2019" + MessageBodyDecoder.MESSAGE_BODY_DECODER_BEAN_SUFFIX)
public class Jt808_2019_0805_MessageBodyDecoder implements MessageBodyDecoder {

    @Override
    public MessageBody decode(byte[] bytes) throws IllegalMessageException {
        Jt808_2019_0805_MessageBody messageBody = new Jt808_2019_0805_MessageBody();
        try {
            int offset = 0;

            byte[] arr = new byte[2];
            System.arraycopy(bytes, offset, arr, 0, arr.length);
            messageBody.setMessageSeq(ByteUtils.word2int(arr));
            offset += arr.length;

            messageBody.setResult(ByteUtils.byte2int(bytes[offset++]));

            if (messageBody.getResult() == 0) {
                arr = new byte[2];
                System.arraycopy(bytes, offset, arr, 0, arr.length);
                int count = ByteUtils.word2int(arr);
                offset += arr.length;

                List<Long> mediaDataIdList = new ArrayList<Long>();
                for (int i = 0; i < count; i++) {
                    arr = new byte[4];
                    System.arraycopy(bytes, offset, arr, 0, arr.length);
                    mediaDataIdList.add(ByteUtils.dword2long(arr));
                    offset += arr.length;
                }
                messageBody.setMediaDataIdList(mediaDataIdList);
            }
            return messageBody;
        } catch (Exception e) {
            throw new IllegalMessageException(e);
        }
    }
}
