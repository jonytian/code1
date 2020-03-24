package com.legaoyi.protocol.messagebody.decoder;

import org.springframework.stereotype.Component;
import com.legaoyi.protocol.exception.IllegalMessageException;
import com.legaoyi.protocol.message.MessageBody;
import com.legaoyi.protocol.message.decoder.MessageBodyDecoder;
import com.legaoyi.protocol.upstream.messagebody.Jt808_2019_0702_MessageBody;
import com.legaoyi.protocol.util.ByteUtils;
import com.legaoyi.protocol.util.DateUtils;

/**
 * @author <a href="mailto:shengbo.gao@gmail.com;78772895@qq.com">gaoshengbo</a>
 * @version 1.0.0
 * @since 2019-05-20
 */
@Component(MessageBodyDecoder.MESSAGE_BODY_DECODER_BEAN_PREFIX + "0702_2019" + MessageBodyDecoder.MESSAGE_BODY_DECODER_BEAN_SUFFIX)
public class Jt808_2019_0702_MessageBodyDecoder implements MessageBodyDecoder {

    @Override
    public MessageBody decode(byte[] bytes) throws IllegalMessageException {
        Jt808_2019_0702_MessageBody messageBody = new Jt808_2019_0702_MessageBody();
        try {
            int offset = 0;

            messageBody.setIcCardState(ByteUtils.byte2int(bytes[offset]));
            offset++;

            byte[] arr = new byte[6];
            System.arraycopy(bytes, offset, arr, 0, arr.length);
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
            messageBody.setIcCardOptTime(icCardOptTime);
            offset += arr.length;

            if (messageBody.getIcCardState() == 1) {
                messageBody.setIcCardReadResult(ByteUtils.byte2int(bytes[offset]));
                offset++;
                if (messageBody.getIcCardReadResult() == 0) {
                    int len = ByteUtils.byte2int(bytes[offset]);
                    offset++;

                    arr = new byte[len];
                    System.arraycopy(bytes, offset, arr, 0, arr.length);
                    messageBody.setDriverName(ByteUtils.bytes2gbk(arr));
                    offset += arr.length;

                    arr = new byte[20];
                    System.arraycopy(bytes, offset, arr, 0, arr.length);
                    messageBody.setQualification(ByteUtils.bytes2gbk(arr));
                    offset += arr.length;

                    len = ByteUtils.byte2int(bytes[offset]);
                    offset++;

                    arr = new byte[len];
                    System.arraycopy(bytes, offset, arr, 0, arr.length);
                    messageBody.setCertifyauth(ByteUtils.bytes2gbk(arr));
                    offset += arr.length;

                    arr = new byte[4];
                    System.arraycopy(bytes, offset, arr, 0, arr.length);
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
                    messageBody.setCertValidDate(sb.deleteCharAt(0).toString());

                    arr = new byte[20];
                    System.arraycopy(bytes, offset, arr, 0, arr.length);
                    messageBody.setIdCard(ByteUtils.bytes2gbk(arr));
                    offset += arr.length;
                }
            }
            return messageBody;
        } catch (Exception e) {
            throw new IllegalMessageException(e);
        }
    }
}
