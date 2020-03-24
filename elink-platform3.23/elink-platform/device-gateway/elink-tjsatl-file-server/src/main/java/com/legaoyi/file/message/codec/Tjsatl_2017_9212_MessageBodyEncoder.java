package com.legaoyi.file.message.codec;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.legaoyi.file.messagebody.Tjsatl_2017_9212_MessageBody;
import com.legaoyi.protocol.exception.IllegalMessageException;
import com.legaoyi.protocol.message.MessageBody;
import com.legaoyi.protocol.message.encoder.MessageBodyEncoder;
import com.legaoyi.protocol.util.ByteUtils;
import com.legaoyi.protocol.util.MessageBuilder;

/**
 * 
 * @author <a href="mailto:shengbo.gao@gmail.com;78772895@qq.com">gaoshengbo</a>
 * @version 1.0.0
 * @since 2019-05-20
 */
@Component(MessageBodyEncoder.MESSAGE_BODY_ENCODER_BEAN_PREFIX + "9212_tjsatl" + MessageBodyEncoder.MESSAGE_BODY_ENCODER_BEAN_SUFFIX)
public class Tjsatl_2017_9212_MessageBodyEncoder implements MessageBodyEncoder {

    @Override
    public byte[] encode(MessageBody messageBody) throws IllegalMessageException {
        try {
            Tjsatl_2017_9212_MessageBody body = (Tjsatl_2017_9212_MessageBody) messageBody;
            MessageBuilder mb = new MessageBuilder();
            byte[] bytes = ByteUtils.gbk2bytes(body.getFileName());
            mb.addByte(bytes.length);
            mb.append(bytes);
            mb.addByte(body.getFileType());
            mb.addByte(body.getResult());

            if (body.getResult() == 0) {
                mb.addByte(0);
            } else {
                List<Map<String, Long>> list = body.getPackageList();
                mb.addByte(list.size());
                for (Map<String, Long> map : list) {
                    mb.append(ByteUtils.long2dword(Long.parseLong(String.valueOf(map.get("offset")))));
                    mb.append(ByteUtils.long2dword(Long.parseLong(String.valueOf(map.get("length")))));
                }
            }
            return mb.getBytes();
        } catch (Exception e) {
            throw new IllegalMessageException(e);
        }
    }
}
