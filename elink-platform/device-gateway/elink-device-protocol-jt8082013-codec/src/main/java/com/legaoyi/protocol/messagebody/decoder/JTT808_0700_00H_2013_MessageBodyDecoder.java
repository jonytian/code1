package com.legaoyi.protocol.messagebody.decoder;

import org.springframework.stereotype.Component;

import com.legaoyi.protocol.exception.IllegalMessageException;
import com.legaoyi.protocol.message.MessageBody;
import com.legaoyi.protocol.message.decoder.MessageBodyDecoder;
import com.legaoyi.protocol.messagebody.decoder.JTT808_0700_2013_MessageBodyDecoder;
import com.legaoyi.protocol.up.messagebody.JTT808_0700_00H_2013_MessageBody;
import com.legaoyi.protocol.util.ByteUtils;

/**
 * 采集记录仪执行标准版本
 * @author <a href="mailto:shengbo.gao@gmail.com;78772895@qq.com">gaoshengbo</a>
 * @version 1.0.0
 * @since 2015-01-30
 */
@Component(MessageBodyDecoder.MESSAGE_BODY_DECODER_BEAN_PREFIX+"0700_00H_2013"+MessageBodyDecoder.MESSAGE_BODY_DECODER_BEAN_SUFFIX)
public class JTT808_0700_00H_2013_MessageBodyDecoder extends JTT808_0700_2013_MessageBodyDecoder {

    @Override
    public MessageBody decode(byte[] messageBody) throws IllegalMessageException {
        JTT808_0700_00H_2013_MessageBody message = new JTT808_0700_00H_2013_MessageBody();
        try {
            int offset = 9;
            message.setYearNo("20" + ByteUtils.bytes2bcd(new byte[] {messageBody[offset]}));
            offset++;
            
            message.setTrackingNo("" + ByteUtils.byte2int(messageBody[offset]));
            return message;
        } catch (Exception e) {
            throw new IllegalMessageException(e);
        }
    }
}
