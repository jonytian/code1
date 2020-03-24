package com.legaoyi.protocol.messagebody.decoder;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import com.legaoyi.protocol.exception.IllegalMessageException;
import com.legaoyi.protocol.message.MessageBody;
import com.legaoyi.protocol.message.decoder.MessageBodyDecoder;
import com.legaoyi.protocol.up.messagebody.JTT1078_1205_MessageBody;
import com.legaoyi.protocol.util.ByteUtils;
import com.legaoyi.protocol.util.DateUtils;

/**
 * @author <a href="mailto:shengbo.gao@gmail.com;78772895@qq.com">gaoshengbo</a>
 * @version 1.0.0
 * @since 2018-04-09
 */
@Component(MessageBodyDecoder.MESSAGE_BODY_DECODER_BEAN_PREFIX + "1205_2016" + MessageBodyDecoder.MESSAGE_BODY_DECODER_BEAN_SUFFIX)
public class JTT1078_1205_MessageBodyDecoder implements MessageBodyDecoder {

    @Override
    public MessageBody decode(byte[] messageBody) throws IllegalMessageException {
        JTT1078_1205_MessageBody message = new JTT1078_1205_MessageBody();
        try {
            int offset = 0;
            byte[] arr = new byte[2];
            System.arraycopy(messageBody, offset, arr, 0, arr.length);
            message.setMessageSeq(ByteUtils.word2int(arr));

            offset += arr.length;
            arr = new byte[4];
            System.arraycopy(messageBody, offset, arr, 0, arr.length);
            int length = (int) ByteUtils.dword2long(arr);
            List<JTT1078_1205_MessageBody.MediaResource> list = new ArrayList<JTT1078_1205_MessageBody.MediaResource>();
            JTT1078_1205_MessageBody.MediaResource mediaResource = null;
            for (int i = 0; i < length; i++) {
                mediaResource = message.new MediaResource();
                offset += arr.length;
                arr = new byte[1];
                System.arraycopy(messageBody, offset, arr, 0, arr.length);
                mediaResource.setChannelId(ByteUtils.byte2int(arr[0]));

                offset += arr.length;
                arr = new byte[6];
                System.arraycopy(messageBody, offset, arr, 0, arr.length);
                String dateStr = ByteUtils.bytes2bcd(arr);
                if ("000000000000".equals(dateStr)) {
                    dateStr = "000101000000";
                }
                String startTime = DateUtils.bcd2dateTime(dateStr);
                mediaResource.setStartTime(startTime);

                offset += arr.length;
                System.arraycopy(messageBody, offset, arr, 0, arr.length);
                dateStr = ByteUtils.bytes2bcd(arr);
                if ("000000000000".equals(dateStr)) {
                    dateStr = "000101000000";
                }
                String endTime = DateUtils.bcd2dateTime(dateStr);
                mediaResource.setEndTime(endTime);

                offset += arr.length;
                arr = new byte[4];
                System.arraycopy(messageBody, offset, arr, 0, arr.length);
                String alarmFlag = ByteUtils.dword2bin(arr);

                offset += arr.length;
                System.arraycopy(messageBody, offset, arr, 0, arr.length);
                alarmFlag = alarmFlag.concat(ByteUtils.dword2bin(arr));

                mediaResource.setAlarmFlag(StringUtils.reverse(alarmFlag));

                offset += arr.length;
                arr = new byte[1];
                System.arraycopy(messageBody, offset, arr, 0, arr.length);
                mediaResource.setResourceType(ByteUtils.byte2int(arr[0]));

                offset += arr.length;
                System.arraycopy(messageBody, offset, arr, 0, arr.length);
                mediaResource.setStreamType(ByteUtils.byte2int(arr[0]));

                offset += arr.length;
                System.arraycopy(messageBody, offset, arr, 0, arr.length);
                mediaResource.setStoreType(ByteUtils.byte2int(arr[0]));

                offset += arr.length;
                arr = new byte[4];
                System.arraycopy(messageBody, offset, arr, 0, arr.length);
                mediaResource.setFileSize(ByteUtils.dword2long(arr));

                list.add(mediaResource);
            }

            message.setResourceList(list);
            return message;
        } catch (Exception e) {
            throw new IllegalMessageException(e);
        }
    }
}
