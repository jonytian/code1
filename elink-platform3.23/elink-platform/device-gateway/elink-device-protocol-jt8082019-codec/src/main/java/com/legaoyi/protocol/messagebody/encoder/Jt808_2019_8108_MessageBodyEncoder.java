package com.legaoyi.protocol.messagebody.encoder;

import org.springframework.stereotype.Component;

import com.legaoyi.protocol.downstream.messagebody.Jt808_2019_8108_MessageBody;
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
@Component(MessageBodyEncoder.MESSAGE_BODY_ENCODER_BEAN_PREFIX + "8108_2019" + MessageBodyEncoder.MESSAGE_BODY_ENCODER_BEAN_SUFFIX)
public class Jt808_2019_8108_MessageBodyEncoder implements MessageBodyEncoder {

    @Override
    public byte[] encode(MessageBody messageBody) throws IllegalMessageException {
        try {
            Jt808_2019_8108_MessageBody body = (Jt808_2019_8108_MessageBody) messageBody;
            MessageBuilder mb = new MessageBuilder();
            mb.addByte(body.getUpgradeType());
            mb.append(ByteUtils.gbk2bytes(body.getMfrsId(), 5));
            byte[] bytes = ByteUtils.gbk2bytes(body.getVersion());
            mb.addByte(bytes.length);
            mb.append(bytes);
            String upgradePackageData = body.getUpgradePackageData();
            bytes = ByteUtils.hex2bytes(upgradePackageData);
            mb.addDword(bytes.length);
            mb.append(bytes);
            return mb.getBytes();
        } catch (Exception e) {
            throw new IllegalMessageException(e);
        }
    }
}
