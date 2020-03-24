package com.legaoyi.protocol.messagebody.decoder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import com.legaoyi.protocol.exception.IllegalMessageException;
import com.legaoyi.protocol.message.MessageBody;
import com.legaoyi.protocol.message.decoder.MessageBodyDecoder;
import com.legaoyi.protocol.messagebody.decoder.JTT808_0700_2013_MessageBodyDecoder;
import com.legaoyi.protocol.up.messagebody.JTT808_0700_08H_2013_MessageBody;
import com.legaoyi.protocol.util.ByteUtils;
import com.legaoyi.protocol.util.DateUtils;

/**
 * @author <a href="mailto:shengbo.gao@gmail.com;78772895@qq.com">gaoshengbo</a>
 * @version 1.0.0
 * @since 2015-01-30
 */
@Component(MessageBodyDecoder.MESSAGE_BODY_DECODER_BEAN_PREFIX + "0700_08H_2013" + MessageBodyDecoder.MESSAGE_BODY_DECODER_BEAN_SUFFIX)
public class JTT808_0700_08H_2013_MessageBodyDecoder extends JTT808_0700_2013_MessageBodyDecoder {

    @Override
    public MessageBody decode(byte[] messageBody) throws IllegalMessageException {
        JTT808_0700_08H_2013_MessageBody message = new JTT808_0700_08H_2013_MessageBody();
        int offset = 9;
        try {
            List<Object> dataList = new ArrayList<Object>();
            Map<String, Object> data;
            StringBuilder sb = null;
            int dataOffset = messageBody.length - 1;
            while (offset < dataOffset) {
                byte[] dataBlock = new byte[126];
                System.arraycopy(messageBody, offset, dataBlock, 0, dataBlock.length);
                offset += dataBlock.length;

                int index = 0;
                byte[] arr = new byte[6];
                System.arraycopy(dataBlock, index, arr, 0, arr.length);
                String startTime = DateUtils.bcd2dateTime(ByteUtils.bytes2bcd(arr));
                index += arr.length;

                sb = new StringBuilder();
                for (int i = 0; i < 60 && index < dataBlock.length; i++) {
                    int avgSpeed = -1;
                    if (dataBlock[index] != 0xff) {
                        avgSpeed = ByteUtils.byte2int(dataBlock[index]);
                    }
                    index++;
                    String state = StringUtils.reverse(ByteUtils.byte2bin(dataBlock[index]));
                    index++;
                    sb.append(";").append(avgSpeed).append(",").append(state);
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
