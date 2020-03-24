package com.legaoyi.protocol.messagebody.decoder;

import org.springframework.stereotype.Component;
import com.legaoyi.protocol.exception.IllegalMessageException;
import com.legaoyi.protocol.message.MessageBody;
import com.legaoyi.protocol.message.decoder.MessageBodyDecoder;
import com.legaoyi.protocol.up.messagebody.JTT808_0700_06H_MessageBody;
import com.legaoyi.protocol.util.ByteUtils;

/**
 * @author <a href="mailto:shengbo.gao@gmail.com;78772895@qq.com">gaoshengbo</a>
 * @version 1.0.0
 * @since 2015-01-30
 */
@Component(MessageBodyDecoder.MESSAGE_BODY_DECODER_BEAN_PREFIX + "0700_06H_2011" + MessageBodyDecoder.MESSAGE_BODY_DECODER_BEAN_SUFFIX)
public class JTT808_0700_06H_MessageBodyDecoder extends JTT808_0700_MessageBodyDecoder {

    @Override
    public MessageBody decode(byte[] messageBody) throws IllegalMessageException {
        JTT808_0700_06H_MessageBody message = new JTT808_0700_06H_MessageBody();
        try {
            int offset = 9;
            
            byte[] arr = new byte[17];
            System.arraycopy(messageBody, offset, arr, 0, arr.length);
            // 符合 上述 要求的 ASCII码字符
            message.setVin(ByteUtils.bytes2ascii(arr));
            offset += arr.length;

            // 符合上述 要求 的汉字 和 ASCII码 字符 。共 使 用 9字节表示 车牌 号，其 中一 个汉字用 2个字节 表示 ，其余字符 用 ASCII码字符 表示 ，多余 3个字 节作为车牌号备用 字
            arr = new byte[2];
            System.arraycopy(messageBody, offset, arr, 0, arr.length);
            String plateNo = ByteUtils.bytes2gb2312(arr);
            offset += arr.length;

            arr = new byte[7];
            System.arraycopy(messageBody, offset, arr, 0, arr.length);
            plateNo += ByteUtils.bytes2ascii(arr);
            offset += arr.length;

            message.setPlateNo(plateNo);
            offset += 3;

            // GA 36规定 的分类 。共使 用前 8字节 表示 车牌分类 ，后 4个字 节为备用字
            arr = new byte[8];
            System.arraycopy(messageBody, offset, arr, 0, arr.length);
            message.setPlateType(ByteUtils.bytes2gb2312(arr));
            
            return message;
        } catch (Exception e) {
            throw new IllegalMessageException(e);
        }
    }
}
