package com.legaoyi.protocol.messagebody.decoder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.legaoyi.protocol.exception.IllegalMessageException;
import com.legaoyi.protocol.message.MessageBody;
import com.legaoyi.protocol.message.decoder.MessageBodyDecoder;
import com.legaoyi.protocol.messagebody.decoder.Jt808_2019_0700_MessageBodyDecoder;
import com.legaoyi.protocol.upstream.messagebody.Jt808_2019_0700_09H_MessageBody;
import com.legaoyi.protocol.util.ByteUtils;
import com.legaoyi.protocol.util.DateUtils;

/**
 * @author <a href="mailto:shengbo.gao@gmail.com;78772895@qq.com">gaoshengbo</a>
 * @version 1.0.0
 * @since 2019-05-20
 */
@Component(MessageBodyDecoder.MESSAGE_BODY_DECODER_BEAN_PREFIX + "0700_09H_2019" + MessageBodyDecoder.MESSAGE_BODY_DECODER_BEAN_SUFFIX)
public class Jt808_2019_0700_09H_MessageBodyDecoder extends Jt808_2019_0700_MessageBodyDecoder {

    @Override
    public MessageBody decode(byte[] bytes) throws IllegalMessageException {
        Jt808_2019_0700_09H_MessageBody messageBody = new Jt808_2019_0700_09H_MessageBody();
        try {
            int offset = 9;

            List<Object> dataList = new ArrayList<Object>();
            Map<String, Object> data;
            StringBuilder sb = null;
            while (offset < bytes.length) {
                byte[] dataBlock = new byte[666];
                System.arraycopy(bytes, offset, dataBlock, 0, dataBlock.length);
                offset += dataBlock.length;

                int index = 0;
                byte[] arr = new byte[6];
                System.arraycopy(dataBlock, index, arr, 0, arr.length);
                String startTime = DateUtils.bcd2dateTime(ByteUtils.bytes2bcd(arr));
                index += arr.length;

                sb = new StringBuilder();
                for (int i = 0; i < 60 && index < dataBlock.length; i++) {
                    byte[] a = new byte[10];
                    System.arraycopy(dataBlock, index, a, 0, a.length);
                    sb.append(";");
                    if ("7FFFFFFFH".equals(ByteUtils.bytes2hex(a))) {
                        index += a.length;
                    } else {
                        arr = new byte[4];
                        System.arraycopy(dataBlock, index, arr, 0, arr.length);
                        String lng = String.valueOf(Double.valueOf(Double.valueOf(ByteUtils.dword2long(arr)).doubleValue() / 1000000.0D));
                        index += arr.length;

                        arr = new byte[4];
                        System.arraycopy(dataBlock, index, arr, 0, arr.length);
                        String lat = String.valueOf(Double.valueOf(Double.valueOf(ByteUtils.dword2long(arr)).doubleValue() / 1000000.0D));
                        index += arr.length;

                        arr = new byte[2];
                        System.arraycopy(dataBlock, index, arr, 0, arr.length);
                        int altitude = ByteUtils.word2int(arr);
                        if (altitude > 32767) {
                            altitude -= 65536;
                        }

                        sb.append(lng).append(",").append(lat).append(",").append(altitude);
                        index += arr.length;
                    }

                    int avgSpeed = -1;
                    if (dataBlock[index] != 0xFF) {
                        avgSpeed = ByteUtils.byte2int(dataBlock[index]);
                    }
                    sb.append(",").append(avgSpeed);
                    index++;
                }
                if (sb.length() > 0) {
                    sb.deleteCharAt(0);
                }

                data = new HashMap<String, Object>();
                data.put("startTime", startTime);
                data.put("dataList", sb.toString());
                dataList.add(data);
            }
            messageBody.setDataList(dataList);
            return messageBody;
        } catch (Exception e) {
            throw new IllegalMessageException(e);
        }
    }

}
