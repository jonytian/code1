package com.legaoyi.protocol.messagebody.encoder;

import java.util.List;
import org.springframework.stereotype.Component;
import com.legaoyi.protocol.down.messagebody.Tjsatl_2017_8900_MessageBody;
import com.legaoyi.protocol.exception.IllegalMessageException;
import com.legaoyi.protocol.message.MessageBody;
import com.legaoyi.protocol.message.encoder.MessageBodyEncoder;
import com.legaoyi.protocol.util.MessageBuilder;

/**
 * 
 * @author <a href="mailto:shengbo.gao@gmail.com;78772895@qq.com">gaoshengbo</a>
 * @version 1.0.0
 * @since 2019-05-20
 */
@Component(MessageBodyEncoder.MESSAGE_BODY_ENCODER_BEAN_PREFIX + "8900_tjsatl" + MessageBodyEncoder.MESSAGE_BODY_ENCODER_BEAN_SUFFIX)
public class Tjsatl_2017_8900_MessageBodyEncoder implements MessageBodyEncoder {

    @Override
    public byte[] encode(MessageBody messageBody) throws IllegalMessageException {
        try {
            Tjsatl_2017_8900_MessageBody body = (Tjsatl_2017_8900_MessageBody) messageBody;
            MessageBuilder mb = new MessageBuilder();

            mb.addByte(body.getType());
            List<Integer> list = body.getIdList();
            mb.addByte(list.size());
            for (Integer id : list) {
                mb.addByte(id);
            }

            return mb.getBytes();
        } catch (Exception e) {
            throw new IllegalMessageException(e);
        }
    }
}
