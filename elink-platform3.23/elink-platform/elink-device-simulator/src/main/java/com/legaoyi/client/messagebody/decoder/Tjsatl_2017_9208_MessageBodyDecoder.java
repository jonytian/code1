package com.legaoyi.client.messagebody.decoder;

import org.springframework.stereotype.Component;
import com.legaoyi.client.down.messagebody.Tjsatl_2017_9208_MessageBody;
import com.legaoyi.protocol.exception.IllegalMessageException;
import com.legaoyi.protocol.message.MessageBody;
import com.legaoyi.protocol.message.decoder.MessageBodyDecoder;
import com.legaoyi.protocol.util.ByteUtils;
import com.legaoyi.protocol.util.DateUtils;

/**
 * 
 * @author <a href="mailto:shengbo.gao@gmail.com;78772895@qq.com">gaoshengbo</a>
 * @version 1.0.0
 * @since 2018-08-07
 */
@Component(MessageBodyDecoder.MESSAGE_BODY_DECODER_BEAN_PREFIX + "9208_tjsatl" + MessageBodyDecoder.MESSAGE_BODY_DECODER_BEAN_SUFFIX)
public class Tjsatl_2017_9208_MessageBodyDecoder implements MessageBodyDecoder {

    @Override
    public MessageBody decode(byte[] messageBody) throws IllegalMessageException {
        Tjsatl_2017_9208_MessageBody body = new Tjsatl_2017_9208_MessageBody();
        try {
            int offset = 0;
            int length = ByteUtils.byte2int(messageBody[offset++]);

            byte[] arr = new byte[length];
            System.arraycopy(messageBody, offset, arr, 0, arr.length);
            body.setIp(ByteUtils.bytes2gbk(arr));
            offset += arr.length;

            arr = new byte[2];
            System.arraycopy(messageBody, offset, arr, 0, arr.length);
            body.setTcpPort(ByteUtils.word2int(arr));
            offset += arr.length;

            System.arraycopy(messageBody, offset, arr, 0, arr.length);
            body.setUdpPort(ByteUtils.word2int(arr));
            offset += arr.length;

            arr = new byte[7];
            System.arraycopy(messageBody, offset, arr, 0, arr.length);
            body.setTerminalId(ByteUtils.bytes2ascii(arr));
            offset += arr.length;

            arr = new byte[6];
            System.arraycopy(messageBody, offset, arr, 0, arr.length);
            body.setAlarmTime(DateUtils.bcd2dateTime(ByteUtils.bytes2bcd(arr)));
            offset += arr.length;

            body.setAlarmSeq(ByteUtils.byte2int(messageBody[offset++]));
            body.setTotalFile(ByteUtils.byte2int(messageBody[offset++]));
            body.setAlarmExt(ByteUtils.byte2int(messageBody[offset++]));

            arr = new byte[32];
            System.arraycopy(messageBody, offset, arr, 0, arr.length);
            body.setAlarmId(ByteUtils.bytes2gbk(arr));
            offset += arr.length;
        } catch (Exception e) {
            throw new IllegalMessageException(e);
        }
        return body;
    }
}
