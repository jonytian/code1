package com.legaoyi.protocol.messagebody.encoder;

import org.springframework.stereotype.Component;

import com.legaoyi.protocol.downstream.messagebody.Jt808_2019_8700_MessageBody;
import com.legaoyi.protocol.downstream.messagebody.Jt808_2019_8701_MessageBody;
import com.legaoyi.protocol.exception.IllegalMessageException;
import com.legaoyi.protocol.message.MessageBody;
import com.legaoyi.protocol.message.encoder.MessageBodyEncoder;
import com.legaoyi.protocol.util.ByteUtils;
import com.legaoyi.protocol.util.MessageBuilder;
import com.legaoyi.protocol.util.SpringBeanUtil;

/**
 * 
 * @author <a href="mailto:shengbo.gao@gmail.com;78772895@qq.com">gaoshengbo</a>
 * @version 1.0.0
 * @since 2019-05-20
 */
@Component(MessageBodyEncoder.MESSAGE_BODY_ENCODER_BEAN_PREFIX + "8701_2019" + MessageBodyEncoder.MESSAGE_BODY_ENCODER_BEAN_SUFFIX)
public class Jt808_2019_8701_MessageBodyEncoder implements MessageBodyEncoder {

    @Override
    public byte[] encode(MessageBody messageBody) throws IllegalMessageException {
        try {
            Jt808_2019_8701_MessageBody body = (Jt808_2019_8701_MessageBody) messageBody;
            MessageBuilder mb = new MessageBuilder();
            byte[] bytes = ByteUtils.hex2bytes(body.getCommandWord().replace("H", "").replace("h", ""));
            mb.append(bytes);
            // 数据块
            mb.append(ByteUtils.hex2bytes(Jt808_2019_8700_MessageBody.HEAD_FLAG));
            mb.append(bytes);

            bytes = null;
            try {
                String messageId = Jt808_2019_8701_MessageBody.MESSAGE_ID.concat("_").concat(body.getCommandWord()).concat("H");
                MessageBodyEncoder messageBodyEncoder = SpringBeanUtil.getMessageBodyEncoder(messageId, "2019");
                bytes = messageBodyEncoder.encode(messageBody);
            } catch (Exception e) {
            }

            if (bytes != null) {
                mb.addWord(bytes.length);
                mb.addByte(0);
                mb.append(bytes);
            } else {
                mb.addWord(0);
                mb.addByte(0);
            }

            bytes = mb.getBytes();
            // 计算校验和
            int crc = ByteUtils.byte2int(bytes[0]);
            for (int i = 1, l = bytes.length; i < l; i++) {
                crc = crc ^ ByteUtils.byte2int(bytes[i]);
            }
            mb.addByte(crc);
            return mb.getBytes();
        } catch (Exception e) {
            throw new IllegalMessageException(e);
        }
    }

}
