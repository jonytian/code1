package com.legaoyi.protocol.messagebody.decoder;

import org.springframework.stereotype.Component;

import com.legaoyi.protocol.exception.IllegalMessageException;
import com.legaoyi.protocol.message.MessageBody;
import com.legaoyi.protocol.message.decoder.MessageBodyDecoder;
import com.legaoyi.protocol.up.messagebody.JTT808_0800_MessageBody;
import com.legaoyi.protocol.util.ByteUtils;

/**
 * @author <a href="mailto:shengbo.gao@gmail.com;78772895@qq.com">gaoshengbo</a>
 * @version 1.0.0
 * @since 2015-01-30
 */
@Component(MessageBodyDecoder.MESSAGE_BODY_DECODER_BEAN_PREFIX+"0800_2011"+MessageBodyDecoder.MESSAGE_BODY_DECODER_BEAN_SUFFIX)
public class JTT808_0800_MessageBodyDecoder implements MessageBodyDecoder {

    @Override
    public MessageBody decode(byte[] messageBody) throws IllegalMessageException {
        JTT808_0800_MessageBody message = new JTT808_0800_MessageBody();
        try {
            int offset = 0;
            byte[] arr = new byte[4];
            System.arraycopy(messageBody, offset, arr, 0, arr.length);
            message.setMediaDataId(ByteUtils.dword2long(arr));
            offset += arr.length;
            message.setMediaType(ByteUtils.byte2int(messageBody[offset++]));
            message.setMediaFormatCode(ByteUtils.byte2int(messageBody[offset++]));
            message.setEventCode(ByteUtils.byte2int(messageBody[offset++]));
            message.setChannelId(ByteUtils.byte2int(messageBody[offset++]));
            return message;
        } catch (Exception e) {
            throw new IllegalMessageException(e);
        }
    }

    protected void copy(MessageBody src, JTT808_0800_MessageBody desc) throws IllegalMessageException {
        JTT808_0800_MessageBody src1 = (JTT808_0800_MessageBody) src;
        desc.setChannelId(src1.getChannelId());
        desc.setEventCode(src1.getEventCode());
        desc.setMediaDataId(src1.getMediaDataId());
        desc.setMediaFormatCode(src1.getMediaFormatCode());
        desc.setMediaType(src1.getMediaType());
    }
}
