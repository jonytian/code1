package com.legaoyi.protocol.messagebody.encoder;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import com.legaoyi.protocol.down.messagebody.JTT808_8302_MessageBody;
import com.legaoyi.protocol.exception.IllegalMessageException;
import com.legaoyi.protocol.message.MessageBody;
import com.legaoyi.protocol.message.encoder.MessageBodyEncoder;
import com.legaoyi.protocol.util.ByteUtils;
import com.legaoyi.protocol.util.MessageBuilder;

/**
 * 
 * @author <a href="mailto:shengbo.gao@gmail.com;78772895@qq.com">gaoshengbo</a>
 * @version 1.0.0
 * @since 2015-01-30
 */
@Component(MessageBodyEncoder.MESSAGE_BODY_ENCODER_BEAN_PREFIX + "8302_2011" + MessageBodyEncoder.MESSAGE_BODY_ENCODER_BEAN_SUFFIX)
public class JTT808_8302_MessageBodyEncoder implements MessageBodyEncoder {

    @Override
    public byte[] encode(MessageBody message) throws IllegalMessageException {
        try {
            JTT808_8302_MessageBody messageBody = (JTT808_8302_MessageBody) message;
            MessageBuilder mb = new MessageBuilder();
            mb.addByte(ByteUtils.bin2int(StringUtils.reverse(messageBody.getFlag())));
            byte[] bytes = ByteUtils.gbk2bytes(messageBody.getQuestion());
            mb.addByte(bytes.length);
            mb.append(bytes);
            List<Map<String, Object>> answerList = messageBody.getAnswerList();
            int size = answerList.size();
            if (size > 0) {
                mb.addByte(size);
                for (Map<String, Object> map : answerList) {
                    int itemId = (Integer) map.get("answerId");
                    String answer = (String) map.get("answer");
                    mb.addByte(itemId);
                    bytes = ByteUtils.gbk2bytes(answer);
                    mb.addWord(bytes.length);
                    mb.append(bytes);
                }
            } else {
                mb.addByte(0);
            }
            return mb.getBytes();
        } catch (Exception e) {
            throw new IllegalMessageException(e);
        }
    }
}
