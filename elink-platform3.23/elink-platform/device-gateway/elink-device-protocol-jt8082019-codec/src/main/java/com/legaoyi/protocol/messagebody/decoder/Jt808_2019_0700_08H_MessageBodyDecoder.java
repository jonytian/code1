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
import com.legaoyi.protocol.messagebody.decoder.Jt808_2019_0700_MessageBodyDecoder;
import com.legaoyi.protocol.upstream.messagebody.Jt808_2019_0700_08H_MessageBody;
import com.legaoyi.protocol.util.ByteUtils;
import com.legaoyi.protocol.util.DateUtils;

/**
 * @author <a href="mailto:shengbo.gao@gmail.com;78772895@qq.com">gaoshengbo</a>
 * @version 1.0.0
 * @since 2019-05-20
 */
@Component(MessageBodyDecoder.MESSAGE_BODY_DECODER_BEAN_PREFIX + "0700_08H_2019" + MessageBodyDecoder.MESSAGE_BODY_DECODER_BEAN_SUFFIX)
public class Jt808_2019_0700_08H_MessageBodyDecoder extends Jt808_2019_0700_MessageBodyDecoder {

    @Override
    public MessageBody decode(byte[] bytes) throws IllegalMessageException {
        Jt808_2019_0700_08H_MessageBody messageBody = new Jt808_2019_0700_08H_MessageBody();

        try {

            int offset = 9;

            List<Object> dataList = new ArrayList<Object>();
            Map<String, Object> data;
            StringBuilder sb = null;
            while (offset < bytes.length) {
                byte[] dataBlock = new byte[126];
                System.arraycopy(bytes, offset, dataBlock, 0, dataBlock.length);
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
            messageBody.setDataList(dataList);

            return messageBody;
        } catch (Exception e) {
            throw new IllegalMessageException(e);
        }
    }
}
