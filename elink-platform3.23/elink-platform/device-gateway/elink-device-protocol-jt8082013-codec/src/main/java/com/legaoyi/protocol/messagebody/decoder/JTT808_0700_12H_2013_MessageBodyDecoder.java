package com.legaoyi.protocol.messagebody.decoder;

import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;

import com.legaoyi.protocol.exception.IllegalMessageException;
import com.legaoyi.protocol.message.MessageBody;
import com.legaoyi.protocol.message.decoder.MessageBodyDecoder;
import com.legaoyi.protocol.messagebody.decoder.JTT808_0700_2013_MessageBodyDecoder;
import com.legaoyi.protocol.up.messagebody.JTT808_0700_12H_2013_MessageBody;
import com.legaoyi.protocol.util.ByteUtils;
import com.legaoyi.protocol.util.DateUtils;

/**
 * @author <a href="mailto:shengbo.gao@gmail.com;78772895@qq.com">gaoshengbo</a>
 * @version 1.0.0
 * @since 2015-01-30
 */
@Component(MessageBodyDecoder.MESSAGE_BODY_DECODER_BEAN_PREFIX + "0700_12H_2013" + MessageBodyDecoder.MESSAGE_BODY_DECODER_BEAN_SUFFIX)
public class JTT808_0700_12H_2013_MessageBodyDecoder extends JTT808_0700_2013_MessageBodyDecoder {

    @Override
    public MessageBody decode(byte[] messageBody) throws IllegalMessageException {
        JTT808_0700_12H_2013_MessageBody message = new JTT808_0700_12H_2013_MessageBody();
        int offset = 9;
        try {
            List<String> dataList = new ArrayList<String>();
            //Map<String, Object> data;
            StringBuilder sb = null;
            int dataOffset = messageBody.length - 1;
            while (offset < dataOffset) {
                //data = new HashMap<String, Object>();
                byte[] dataBlock = new byte[25];
                System.arraycopy(messageBody, offset, dataBlock, 0, dataBlock.length);
                offset += dataBlock.length;

                int index = 0;
                byte[] arr = new byte[6];
                System.arraycopy(dataBlock, index, arr, 0, arr.length);
                String eventTime = DateUtils.bcd2dateTime(ByteUtils.bytes2bcd(arr));
                //data.put("eventTime", DateUtils.getBCDFormatTime(ByteUtils.byteBCDToNumString(arr)));
                index += arr.length;

                arr = new byte[18];
                System.arraycopy(dataBlock, index, arr, 0, arr.length);
                String driverLicense = ByteUtils.bytes2ascii(arr);
                //data.put("driverLicense", StringUtil.trimZero(ByteUtils.byteToString(arr)));
                index += arr.length;
                int eventType = ByteUtils.byte2int(dataBlock[index++]);
                //data.put("eventType", ByteUtils.byteToInt(dataBlock[index++]));
                
                sb = new StringBuilder();
                sb.append(driverLicense).append(",").append(eventTime).append(",").append(eventType);
                dataList.add(sb.toString());
            }
            message.setDataList(dataList);
            return message;
        } catch (Exception e) {
            throw new IllegalMessageException(e);
        }
    }
}
