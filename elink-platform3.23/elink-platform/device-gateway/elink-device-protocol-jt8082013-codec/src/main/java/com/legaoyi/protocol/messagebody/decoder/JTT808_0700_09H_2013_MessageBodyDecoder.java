package com.legaoyi.protocol.messagebody.decoder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.legaoyi.protocol.exception.IllegalMessageException;
import com.legaoyi.protocol.message.MessageBody;
import com.legaoyi.protocol.message.decoder.MessageBodyDecoder;
import com.legaoyi.protocol.messagebody.decoder.JTT808_0700_2013_MessageBodyDecoder;
import com.legaoyi.protocol.up.messagebody.JTT808_0700_09H_2013_MessageBody;
import com.legaoyi.protocol.util.ByteUtils;
import com.legaoyi.protocol.util.DateUtils;

/**
 * @author <a href="mailto:shengbo.gao@gmail.com;78772895@qq.com">gaoshengbo</a>
 * @version 1.0.0
 * @since 2015-01-30
 */
@Component(MessageBodyDecoder.MESSAGE_BODY_DECODER_BEAN_PREFIX + "0700_09H_2013" + MessageBodyDecoder.MESSAGE_BODY_DECODER_BEAN_SUFFIX)
public class JTT808_0700_09H_2013_MessageBodyDecoder extends JTT808_0700_2013_MessageBodyDecoder {

    @Override
    public MessageBody decode(byte[] messageBody) throws IllegalMessageException {
        JTT808_0700_09H_2013_MessageBody message = new JTT808_0700_09H_2013_MessageBody();
        int offset = 9;
        
        try {
            List<Object> dataList = new ArrayList<Object>();
            Map<String, Object> data;
            StringBuilder sb = null;
            int dataOffset = messageBody.length - 1;
            while (offset < dataOffset) {
                byte[] dataBlock = new byte[666];
                System.arraycopy(messageBody, offset, dataBlock, 0, dataBlock.length);
                offset += dataBlock.length;

                int index = 0;
                byte[] arr = new byte[6];
                System.arraycopy(dataBlock, index, arr, 0, arr.length);
                String startTime = DateUtils.bcd2dateTime(ByteUtils.bytes2bcd(arr));
                index += arr.length;

                sb = new StringBuilder();
                for (int i = 0; i < 60 && index < dataBlock.length; i++) {
                    byte[] bytes = new byte[10];
                    System.arraycopy(dataBlock, index, bytes, 0, bytes.length);
                    sb.append(";");
                    if ("7FFFFFFFH".equals(ByteUtils.bytes2hex(bytes))) {
                        index += bytes.length;
                    } else {
                        arr = new byte[4];
                        System.arraycopy(dataBlock, index, arr, 0, arr.length);
                        String lng = String.valueOf(Double.valueOf(Double.valueOf(ByteUtils.dword2long(arr)).doubleValue() / 1000000.0D));
                        // data.put("lat",
                        // String.valueOf(Double.valueOf(Double.valueOf(ByteUtils.dword2long(arr)).doubleValue()
                        // / 1000000.0D)));
                        index += arr.length;

                        arr = new byte[4];
                        System.arraycopy(dataBlock, index, arr, 0, arr.length);
                        String lat = String.valueOf(Double.valueOf(Double.valueOf(ByteUtils.dword2long(arr)).doubleValue() / 1000000.0D));
                        // data.put("lng",
                        // String.valueOf(Double.valueOf(Double.valueOf(ByteUtils.dword2long(arr)).doubleValue()
                        // / 1000000.0D)));
                        index += arr.length;

                        arr = new byte[2];
                        System.arraycopy(dataBlock, index, arr, 0, arr.length);
                        int altitude = ByteUtils.word2int(arr);
                        if (altitude > 32767) {
                            altitude -= 65536;
                        }
                        // data.put("altitude", altitude);
                        sb.append(lng).append(",").append(lat).append(",").append(altitude);
                        index += arr.length;
                    }

                    int avgSpeed = -1;
                    if (dataBlock[index] != 0xFF) {
                        avgSpeed = ByteUtils.byte2int(dataBlock[index]);
                        // map.put("avgSpeed", ByteUtils.byteToInt(dataBlock[index]));
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
            message.setDataList(dataList);
            return message;
        } catch (Exception e) {
            throw new IllegalMessageException(e);
        }
    }

}
