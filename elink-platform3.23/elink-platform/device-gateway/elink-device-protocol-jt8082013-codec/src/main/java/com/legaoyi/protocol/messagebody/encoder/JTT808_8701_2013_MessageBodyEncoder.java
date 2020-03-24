package com.legaoyi.protocol.messagebody.encoder;

import org.springframework.stereotype.Component;

import com.legaoyi.protocol.down.messagebody.JTT808_8701_2013_MessageBody;
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
 * @since 2015-01-30
 */
@Component(MessageBodyEncoder.MESSAGE_BODY_ENCODER_BEAN_PREFIX + "8701_2013" + MessageBodyEncoder.MESSAGE_BODY_ENCODER_BEAN_SUFFIX)
public class JTT808_8701_2013_MessageBodyEncoder implements MessageBodyEncoder {

    @Override
    public byte[] encode(MessageBody message) throws IllegalMessageException {
        try {
            JTT808_8701_2013_MessageBody messageBody = (JTT808_8701_2013_MessageBody) message;
            MessageBuilder mb = new MessageBuilder();
            String commandWord = messageBody.getCommandWord().replace("H", "").replace("h", "");
            byte[] bytes = ByteUtils.hex2bytes(commandWord);
            mb.append(bytes);
            // 数据块，是否包含完整的数据帧？todo
            mb.append(ByteUtils.hex2bytes(JTT808_8701_2013_MessageBody.HEAD_FLAG));
            mb.append(bytes);

            try {
                String messageId = JTT808_8701_2013_MessageBody.MESSAGE_ID.concat("_").concat(commandWord).concat("H");
                MessageBodyEncoder messageBodyEncoder = SpringBeanUtil.getMessageBodyEncoder(messageId, "2013");
                byte[] data = messageBodyEncoder.encode(message);
                mb.append(ByteUtils.int2word(data.length));
                mb.addByte(0);
                mb.append(data);
            } catch (IllegalMessageException e) {
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
