package com.legaoyi.protocol.messagebody.encoder;

import org.springframework.stereotype.Component;

import com.legaoyi.protocol.down.messagebody.JTT808_8701_82H_MessageBody;
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
@Component(MessageBodyEncoder.MESSAGE_BODY_ENCODER_BEAN_PREFIX + "8701_82H_2011" + MessageBodyEncoder.MESSAGE_BODY_ENCODER_BEAN_SUFFIX)
public class JTT808_8701_82H_MessageBodyEncoder extends JTT808_8701_MessageBodyEncoder {

    @Override
    public byte[] encode(MessageBody message) throws IllegalMessageException {
        try {
            JTT808_8701_82H_MessageBody messageBody = (JTT808_8701_82H_MessageBody) message;
            MessageBuilder mb = new MessageBuilder();
            mb.append(ByteUtils.ascii2bytes(messageBody.getVin(), 17));
            // 符合上述 要求 的汉字 和 ASCn码 字符 。共 使用 9字节表示 车牌 号，其中一 个汉字用 2个字节表 示 ，其余字符 用 ASC H码字符 表示 ，多余 3个字 节作为车牌号备用 字
            mb.append(ByteUtils.gb23122bytes(messageBody.getPlateNo().substring(0, 1), 2));
            mb.append(ByteUtils.ascii2bytes(messageBody.getPlateNo().substring(1), 10));
            mb.append(ByteUtils.gb23122bytes(messageBody.getPlateType(), 12));
            return mb.getBytes();
        } catch (Exception e) {
            throw new IllegalMessageException(e);
        }
    }
}
