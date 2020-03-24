package com.legaoyi.protocol.messagebody.decoder;

import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;

import com.legaoyi.protocol.exception.IllegalMessageException;
import com.legaoyi.protocol.message.MessageBody;
import com.legaoyi.protocol.message.decoder.MessageBodyDecoder;
import com.legaoyi.protocol.messagebody.decoder.Jt808_2019_0700_MessageBodyDecoder;
import com.legaoyi.protocol.upstream.messagebody.Jt808_2019_0700_12H_MessageBody;
import com.legaoyi.protocol.util.ByteUtils;
import com.legaoyi.protocol.util.DateUtils;

/**
 * @author <a href="mailto:shengbo.gao@gmail.com;78772895@qq.com">gaoshengbo</a>
 * @version 1.0.0
 * @since 2019-05-20
 */
@Component(MessageBodyDecoder.MESSAGE_BODY_DECODER_BEAN_PREFIX + "0700_12H_2019" + MessageBodyDecoder.MESSAGE_BODY_DECODER_BEAN_SUFFIX)
public class Jt808_2019_0700_12H_MessageBodyDecoder extends Jt808_2019_0700_MessageBodyDecoder {

    @Override
    public MessageBody decode(byte[] bytes) throws IllegalMessageException {
        Jt808_2019_0700_12H_MessageBody messageBody = new Jt808_2019_0700_12H_MessageBody();
        try {
            int offset = 9;

            List<String> dataList = new ArrayList<String>();
            StringBuilder sb = null;
            while (offset < bytes.length) {
                byte[] dataBlock = new byte[25];
                System.arraycopy(bytes, offset, dataBlock, 0, dataBlock.length);
                offset += dataBlock.length;

                int index = 0;
                byte[] arr = new byte[6];
                System.arraycopy(dataBlock, index, arr, 0, arr.length);
                String eventTime = DateUtils.bcd2dateTime(ByteUtils.bytes2bcd(arr));
                index += arr.length;

                arr = new byte[18];
                System.arraycopy(dataBlock, index, arr, 0, arr.length);
                String driverLicense = ByteUtils.bytes2ascii(arr);
                index += arr.length;
                int eventType = ByteUtils.byte2int(dataBlock[index++]);

                sb = new StringBuilder();
                sb.append(driverLicense).append(",").append(eventTime).append(",").append(eventType);
                dataList.add(sb.toString());
            }
            messageBody.setDataList(dataList);

            return messageBody;
        } catch (Exception e) {
            throw new IllegalMessageException(e);
        }
    }
}
