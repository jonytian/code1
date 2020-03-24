package com.legaoyi.protocol.messagebody.decoder;

import org.springframework.stereotype.Component;

import com.legaoyi.protocol.exception.IllegalMessageException;
import com.legaoyi.protocol.message.MessageBody;
import com.legaoyi.protocol.message.decoder.MessageBodyDecoder;
import com.legaoyi.protocol.up.messagebody.JTT808_0700_MessageBody;
import com.legaoyi.protocol.util.ByteUtils;
import com.legaoyi.protocol.util.SpringBeanUtil;

/**
 * @author <a href="mailto:shengbo.gao@gmail.com;78772895@qq.com">gaoshengbo</a>
 * @version 1.0.0
 * @since 2015-01-30
 */
@Component(MessageBodyDecoder.MESSAGE_BODY_DECODER_BEAN_PREFIX + "0700_2011" + MessageBodyDecoder.MESSAGE_BODY_DECODER_BEAN_SUFFIX)
public class JTT808_0700_MessageBodyDecoder implements MessageBodyDecoder {

    @Override
    public MessageBody decode(byte[] messageBody) throws IllegalMessageException {
        JTT808_0700_MessageBody message = new JTT808_0700_MessageBody();
        try {
            int offset = 0;
            byte[] arr = new byte[2];
            System.arraycopy(messageBody, offset, arr, 0, arr.length);
            message.setMessageSeq(ByteUtils.word2int(arr));
            offset += arr.length;

            message.setCommandWord(ByteUtils.byte2hex(messageBody[offset]));
            offset++;

            arr = new byte[messageBody.length - offset];
            System.arraycopy(messageBody, offset, arr, 0, arr.length);
            message.setHexBodyData(ByteUtils.bytes2hex(arr));// for 809部标检测

            JTT808_0700_MessageBody messageBody1 = null;
            try {
                String messageId = JTT808_0700_MessageBody.MESSAGE_ID.concat("_").concat(message.getCommandWord()).concat("H");
                MessageBodyDecoder messageBodyDecoder = SpringBeanUtil.getMessageBodyDecoder(messageId, "2011");
                messageBody1 = (JTT808_0700_MessageBody) messageBodyDecoder.decode(messageBody);
            } catch (Exception e) {
            }

            if (messageBody1 != null) {
                messageBody1.setCommandWord(message.getCommandWord());
                messageBody1.setHexBodyData(message.getHexBodyData());
                messageBody1.setMessageSeq(message.getMessageSeq());
                message = messageBody1;
            }

            return message;
        } catch (Exception e) {
            throw new IllegalMessageException(e);
        }
    }
}
