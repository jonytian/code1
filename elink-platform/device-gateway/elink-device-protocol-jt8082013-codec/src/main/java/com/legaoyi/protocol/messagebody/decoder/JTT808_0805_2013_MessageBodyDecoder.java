package com.legaoyi.protocol.messagebody.decoder;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.legaoyi.protocol.exception.IllegalMessageException;
import com.legaoyi.protocol.message.MessageBody;
import com.legaoyi.protocol.message.decoder.MessageBodyDecoder;
import com.legaoyi.protocol.up.messagebody.JTT808_0805_2013_MessageBody;
import com.legaoyi.protocol.util.ByteUtils;

/**
 * @author <a href="mailto:shengbo.gao@gmail.com;78772895@qq.com">gaoshengbo</a>
 * @version 1.0.0
 * @since 2015-01-30
 */
@Component(MessageBodyDecoder.MESSAGE_BODY_DECODER_BEAN_PREFIX + "0805_2013" + MessageBodyDecoder.MESSAGE_BODY_DECODER_BEAN_SUFFIX)
public class JTT808_0805_2013_MessageBodyDecoder implements MessageBodyDecoder {

    @Override
    public MessageBody decode(byte[] messageBody) throws IllegalMessageException {
        JTT808_0805_2013_MessageBody message = new JTT808_0805_2013_MessageBody();
        try {
            int offset = 0;
            byte[] arr = new byte[2];
            System.arraycopy(messageBody, offset, arr, 0, arr.length);
            message.setMessageSeq(ByteUtils.word2int(arr));
            offset += arr.length;
            
            message.setResult(ByteUtils.byte2int(messageBody[offset++]));

            if (message.getResult() == 0) {
                arr = new byte[2];
                System.arraycopy(messageBody, offset, arr, 0, arr.length);
                int count = ByteUtils.word2int(arr);
                offset += arr.length;

                List<Long> mediaDataIdList = new ArrayList<Long>();
                for (int i = 0; i < count; i++) {
                    arr = new byte[4];
                    System.arraycopy(messageBody, offset, arr, 0, arr.length);
                    mediaDataIdList.add(ByteUtils.dword2long(arr));
                    offset += arr.length;
                }
                message.setMediaDataIdList(mediaDataIdList);
            }
            return message;
        } catch (Exception e) {
            throw new IllegalMessageException(e);
        }
    }
}
