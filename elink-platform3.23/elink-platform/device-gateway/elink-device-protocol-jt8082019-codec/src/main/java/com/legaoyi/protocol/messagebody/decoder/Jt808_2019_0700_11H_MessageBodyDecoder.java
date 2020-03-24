package com.legaoyi.protocol.messagebody.decoder;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.legaoyi.protocol.exception.IllegalMessageException;
import com.legaoyi.protocol.message.MessageBody;
import com.legaoyi.protocol.message.decoder.MessageBodyDecoder;
import com.legaoyi.protocol.messagebody.decoder.Jt808_2019_0700_MessageBodyDecoder;
import com.legaoyi.protocol.upstream.messagebody.Jt808_2019_0700_11H_MessageBody;
import com.legaoyi.protocol.util.ByteUtils;
import com.legaoyi.protocol.util.DateUtils;

/**
 * @author <a href="mailto:shengbo.gao@gmail.com;78772895@qq.com">gaoshengbo</a>
 * @version 1.0.0
 * @since 2019-05-20
 */
@Component(MessageBodyDecoder.MESSAGE_BODY_DECODER_BEAN_PREFIX + "0700_11H_2019" + MessageBodyDecoder.MESSAGE_BODY_DECODER_BEAN_SUFFIX)
public class Jt808_2019_0700_11H_MessageBodyDecoder extends Jt808_2019_0700_MessageBodyDecoder {

    @Override
    public MessageBody decode(byte[] bytes) throws IllegalMessageException {
        Jt808_2019_0700_11H_MessageBody messageBody = new Jt808_2019_0700_11H_MessageBody();

        try {

            int offset = 9;

            List<String> dataList = new ArrayList<String>();
            StringBuilder sb = null;
            while (offset < bytes.length) {
                sb = new StringBuilder();
                byte[] dataBlock = new byte[50];
                System.arraycopy(bytes, offset, dataBlock, 0, dataBlock.length);
                offset += dataBlock.length;

                int index = 0;
                byte[] arr = new byte[18];
                System.arraycopy(dataBlock, index, arr, 0, arr.length);
                String driverLicense = ByteUtils.bytes2ascii(arr);
                index += arr.length;

                arr = new byte[6];
                System.arraycopy(dataBlock, index, arr, 0, arr.length);
                String startTime = DateUtils.bcd2dateTime(ByteUtils.bytes2bcd(arr));
                index += arr.length;

                arr = new byte[6];
                System.arraycopy(dataBlock, index, arr, 0, arr.length);
                String endTime = DateUtils.bcd2dateTime(ByteUtils.bytes2bcd(arr));
                index += arr.length;

                arr = new byte[4];
                System.arraycopy(dataBlock, index, arr, 0, arr.length);
                String startLng = String.valueOf(Double.valueOf(Double.valueOf(ByteUtils.dword2long(arr)).doubleValue() / 1000000.0D));
                index += arr.length;

                arr = new byte[4];
                System.arraycopy(dataBlock, index, arr, 0, arr.length);
                String startLat = String.valueOf(Double.valueOf(Double.valueOf(ByteUtils.dword2long(arr)).doubleValue() / 1000000.0D));
                index += arr.length;

                arr = new byte[2];
                System.arraycopy(dataBlock, index, arr, 0, arr.length);
                int startAltitude = ByteUtils.word2int(arr);
                if (startAltitude > 32767) {
                    startAltitude -= 65536;
                }
                index += arr.length;

                arr = new byte[4];
                System.arraycopy(dataBlock, index, arr, 0, arr.length);
                String endLng = String.valueOf(Double.valueOf(Double.valueOf(ByteUtils.dword2long(arr)).doubleValue() / 1000000.0D));
                index += arr.length;

                arr = new byte[4];
                System.arraycopy(dataBlock, index, arr, 0, arr.length);
                String endLat = String.valueOf(Double.valueOf(Double.valueOf(ByteUtils.dword2long(arr)).doubleValue() / 1000000.0D));
                index += arr.length;

                arr = new byte[2];
                System.arraycopy(dataBlock, index, arr, 0, arr.length);
                int endAltitude = ByteUtils.word2int(arr);
                if (endAltitude > 32767) {
                    endAltitude -= 65536;
                }
                index += arr.length;

                sb.append(driverLicense).append(",").append(startTime).append(",").append(endTime).append(",").append(startLng).append(",").append(startLat).append(",").append(startAltitude).append(",").append(endLng).append(",").append(endLat)
                        .append(",").append(endAltitude);
                dataList.add(sb.toString());
            }

            messageBody.setDataList(dataList);
            return messageBody;
        } catch (Exception e) {
            throw new IllegalMessageException(e);
        }
    }
}
