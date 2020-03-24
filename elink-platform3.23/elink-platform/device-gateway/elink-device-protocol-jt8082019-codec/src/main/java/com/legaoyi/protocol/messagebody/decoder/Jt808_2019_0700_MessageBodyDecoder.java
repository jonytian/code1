package com.legaoyi.protocol.messagebody.decoder;

import org.springframework.stereotype.Component;

import com.legaoyi.protocol.exception.IllegalMessageException;
import com.legaoyi.protocol.message.MessageBody;
import com.legaoyi.protocol.message.decoder.MessageBodyDecoder;
import com.legaoyi.protocol.upstream.messagebody.Jt808_2019_0700_MessageBody;
import com.legaoyi.protocol.util.ByteUtils;
import com.legaoyi.protocol.util.SpringBeanUtil;

/**
 * @author <a href="mailto:shengbo.gao@gmail.com;78772895@qq.com">gaoshengbo</a>
 * @version 1.0.0
 * @since 2019-05-20
 */
@Component(MessageBodyDecoder.MESSAGE_BODY_DECODER_BEAN_PREFIX + "700_2019" + MessageBodyDecoder.MESSAGE_BODY_DECODER_BEAN_SUFFIX)
public class Jt808_2019_0700_MessageBodyDecoder implements MessageBodyDecoder {

    @Override
    public MessageBody decode(byte[] bytes) throws IllegalMessageException {
        Jt808_2019_0700_MessageBody messageBody = new Jt808_2019_0700_MessageBody();
        try {
            int offset = 0;

            byte[] arr = new byte[2];
            System.arraycopy(bytes, offset, arr, 0, arr.length);
            messageBody.setMessageSeq(ByteUtils.word2int(arr));
            offset += arr.length;

            messageBody.setCommandWord(ByteUtils.byte2hex(bytes[offset]));
            offset++;

            arr = new byte[bytes.length - offset];
            System.arraycopy(bytes, offset, arr, 0, arr.length);
            messageBody.setHexBodyData(ByteUtils.bytes2hex(arr));// for 809部标检测

            Jt808_2019_0700_MessageBody messageBody1 = null;
            try {
                String messageId = Jt808_2019_0700_MessageBody.MESSAGE_ID.concat("_").concat(messageBody.getCommandWord());
                MessageBodyDecoder messageBodyDecoder = SpringBeanUtil.getMessageBodyDecoder(messageId, "2019");
                messageBody1 = (Jt808_2019_0700_MessageBody) messageBodyDecoder.decode(bytes);
            } catch (Exception e) {
            }

            if (messageBody1 != null) {
                messageBody1.setCommandWord(messageBody.getCommandWord());
                messageBody1.setHexBodyData(messageBody.getHexBodyData());
                messageBody1.setMessageSeq(messageBody.getMessageSeq());
                messageBody = messageBody1;
            }

            return messageBody;
        } catch (Exception e) {
            throw new IllegalMessageException(e);
        }
    }
}
