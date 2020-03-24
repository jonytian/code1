package com.legaoyi.client.messagebody.decoder;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import com.legaoyi.client.down.messagebody.Jt1078_9205_MessageBody;
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
@Component(MessageBodyDecoder.MESSAGE_BODY_DECODER_BEAN_PREFIX + "9205_1078" + MessageBodyDecoder.MESSAGE_BODY_DECODER_BEAN_SUFFIX)
public class JTT1078_9205_MessageBodyDecoder implements MessageBodyDecoder {

    @Override
    public MessageBody decode(byte[] messageBody) throws IllegalMessageException {
        Jt1078_9205_MessageBody jttMessageBody = new Jt1078_9205_MessageBody();
        try {
            int offset = 0;
            jttMessageBody.setChannelId(ByteUtils.byte2int(messageBody[offset]));
            offset++;

            byte[] arr = new byte[6];
            System.arraycopy(messageBody, offset, arr, 0, arr.length);
            String startTime = DateUtils.bcd2dateTime(ByteUtils.bytes2bcd(arr));
            jttMessageBody.setStartTime(startTime);
            offset += arr.length;

            System.arraycopy(messageBody, offset, arr, 0, arr.length);
            String endTime = DateUtils.bcd2dateTime(ByteUtils.bytes2bcd(arr));
            jttMessageBody.setEndTime(endTime);
            offset += arr.length;

            arr = new byte[4];
            System.arraycopy(messageBody, offset, arr, 0, arr.length);
            String alarmFlag = ByteUtils.dword2bin(arr);
            offset += arr.length;

            System.arraycopy(messageBody, offset, arr, 0, arr.length);
            alarmFlag = alarmFlag.concat(ByteUtils.dword2bin(arr));
            jttMessageBody.setAlarmFlag(StringUtils.reverse(alarmFlag));
            offset += arr.length;

            jttMessageBody.setResourceType(ByteUtils.byte2int(messageBody[offset]));
            offset++;

            jttMessageBody.setStreamType(ByteUtils.byte2int(messageBody[offset]));
            offset++;

            jttMessageBody.setStoreType(ByteUtils.byte2int(messageBody[offset]));
        } catch (Exception e) {
            throw new IllegalMessageException(e);
        }
        return jttMessageBody;
    }
}
