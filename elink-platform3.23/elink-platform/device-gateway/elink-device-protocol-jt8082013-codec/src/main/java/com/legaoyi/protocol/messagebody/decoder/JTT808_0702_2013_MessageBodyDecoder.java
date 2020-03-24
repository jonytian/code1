package com.legaoyi.protocol.messagebody.decoder;

import org.springframework.stereotype.Component;
import com.legaoyi.protocol.exception.IllegalMessageException;
import com.legaoyi.protocol.message.MessageBody;
import com.legaoyi.protocol.message.decoder.MessageBodyDecoder;
import com.legaoyi.protocol.up.messagebody.JTT808_0702_2013_MessageBody;
import com.legaoyi.protocol.util.ByteUtils;
import com.legaoyi.protocol.util.DateUtils;

/**
 * @author <a href="mailto:shengbo.gao@gmail.com;78772895@qq.com">gaoshengbo</a>
 * @version 1.0.0
 * @since 2015-01-30
 */
@Component(MessageBodyDecoder.MESSAGE_BODY_DECODER_BEAN_PREFIX + "0702_2013" + MessageBodyDecoder.MESSAGE_BODY_DECODER_BEAN_SUFFIX)
public class JTT808_0702_2013_MessageBodyDecoder implements MessageBodyDecoder {

    @Override
    public MessageBody decode(byte[] messageBody) throws IllegalMessageException {
        JTT808_0702_2013_MessageBody message = new JTT808_0702_2013_MessageBody();
        try {
            int offset = 0;
            message.setIcCardState(ByteUtils.byte2int(messageBody[offset]));
            offset++;

            byte[] arr = new byte[6];
            System.arraycopy(messageBody, offset, arr, 0, arr.length);
            String icCardOptTime;
            if (13 == arr[0]) {
                String[] dateStr = ByteUtils.bytes2gbk(arr).split(" ");
                icCardOptTime = "20" + dateStr[0] + "-" + dateStr[1] + "-" + dateStr[2] + " " + dateStr[3] + ":" + dateStr[4] + ":" + dateStr[5];
            } else {
                String dateStr = ByteUtils.bytes2bcd(arr);
                if ("000000000000".equals(dateStr)) {
                    dateStr = "000101000000";
                }
                icCardOptTime = DateUtils.bcd2dateTime(dateStr);
            }
            message.setIcCardOptTime(icCardOptTime);
            offset += arr.length;

            if (message.getIcCardState() == 1) {
                message.setIcCardReadResult(ByteUtils.byte2int(messageBody[offset]));
                offset++;
                if (message.getIcCardReadResult() == 0) {
                    int len = ByteUtils.byte2int(messageBody[offset]);
                    offset++;

                    arr = new byte[len];
                    System.arraycopy(messageBody, offset, arr, 0, arr.length);
                    message.setDriverName(ByteUtils.bytes2gbk(arr));
                    offset += arr.length;

                    arr = new byte[20];
                    System.arraycopy(messageBody, offset, arr, 0, arr.length);
                    message.setQualification(ByteUtils.bytes2gbk(arr));
                    offset += arr.length;

                    len = ByteUtils.byte2int(messageBody[offset]);
                    offset++;

                    arr = new byte[len];
                    System.arraycopy(messageBody, offset, arr, 0, arr.length);
                    message.setCertifyauth(ByteUtils.bytes2gbk(arr));
                    offset += arr.length;

                    arr = new byte[4];
                    System.arraycopy(messageBody, offset, arr, 0, arr.length);
                    String dateStr = ByteUtils.bytes2bcd(arr);
                    StringBuffer sb = new StringBuffer(dateStr);
                    for (int i = 0; i < sb.length(); i++) {
                        if (i % 3 == 0) {
                            if (i > 8) {
                                sb.insert(i, ":");
                            } else {
                                sb.insert(i, "-");
                            }
                        }
                    }
                    message.setCertValidDate(sb.deleteCharAt(0).toString());
                }
            }
            return message;
        } catch (Exception e) {
            throw new IllegalMessageException(e);
        }
    }
}
