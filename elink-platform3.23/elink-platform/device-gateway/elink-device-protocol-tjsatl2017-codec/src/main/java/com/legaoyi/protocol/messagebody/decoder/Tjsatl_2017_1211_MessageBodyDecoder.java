package com.legaoyi.protocol.messagebody.decoder;

import org.springframework.stereotype.Component;

import com.legaoyi.protocol.exception.IllegalMessageException;
import com.legaoyi.protocol.message.MessageBody;
import com.legaoyi.protocol.message.decoder.MessageBodyDecoder;
import com.legaoyi.protocol.up.messagebody.Tjsatl_2017_1211_MessageBody;
import com.legaoyi.protocol.util.ByteUtils;

/**
 * @author <a href="mailto:shengbo.gao@gmail.com;78772895@qq.com">gaoshengbo</a>
 * @version 1.0.0
 * @since 2019-05-20
 */
@Component(MessageBodyDecoder.MESSAGE_BODY_DECODER_BEAN_PREFIX + "1211_tjsatl" + MessageBodyDecoder.MESSAGE_BODY_DECODER_BEAN_SUFFIX)
public class Tjsatl_2017_1211_MessageBodyDecoder implements MessageBodyDecoder {

    @Override
    public MessageBody decode(byte[] bytes) throws IllegalMessageException {
        Tjsatl_2017_1211_MessageBody messageBody = new Tjsatl_2017_1211_MessageBody();
        try {
            int offset = 0;

            int length = ByteUtils.byte2int(bytes[offset++]);
            byte[] arr = new byte[length];
            System.arraycopy(bytes, offset, arr, 0, arr.length);
            messageBody.setFileName(ByteUtils.bytes2gbk(arr));
            offset += arr.length;

            messageBody.setFileType(ByteUtils.byte2int(bytes[offset++]));

            arr = new byte[4];
            System.arraycopy(bytes, offset, arr, 0, arr.length);
            messageBody.setFileSize(ByteUtils.dword2long(arr));

            return messageBody;
        } catch (Exception e) {
            throw new IllegalMessageException(e);
        }
    }
}
