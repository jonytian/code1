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
import com.legaoyi.protocol.up.messagebody.JTT808_0700_07H_MessageBody;
import com.legaoyi.protocol.util.ByteUtils;
import com.legaoyi.protocol.util.DateUtils;

/**
 * 采集最近360h内的累计行驶里程数
 * 
 * @author <a href="mailto:shengbo.gao@gmail.com;78772895@qq.com">gaoshengbo</a>
 * @version 1.0.0
 * @since 2015-01-30
 */
@Component(MessageBodyDecoder.MESSAGE_BODY_DECODER_BEAN_PREFIX + "0700_07H_2011" + MessageBodyDecoder.MESSAGE_BODY_DECODER_BEAN_SUFFIX)
public class JTT808_0700_07H_MessageBodyDecoder extends JTT808_0700_MessageBodyDecoder {

    @Override
    public MessageBody decode(byte[] messageBody) throws IllegalMessageException {
        JTT808_0700_07H_MessageBody message = new JTT808_0700_07H_MessageBody();
        try {
            int offset = 9;

            if (messageBody.length - offset > 0) {
                int count = (int) Math.floor((messageBody.length - 1 - offset) / 206);
                List<Object> accidentList = new ArrayList<Object>();
                StringBuilder sb = null;
                for (int i = 0; i < count; i++) {
                    byte[] arr = new byte[6];
                    System.arraycopy(messageBody, offset, arr, 0, arr.length);
                    String eventTime = DateUtils.bcd2dateTime(ByteUtils.bytes2bcd(arr));
                    offset += arr.length;

                    sb = new StringBuilder();
                    for (int j = 0; j < 100; j++) {
                        int speed = ByteUtils.byte2int(messageBody[offset++]);
                        String state = StringUtils.reverse(ByteUtils.byte2bin(messageBody[offset++]));
                        sb.append(";").append(speed).append(",").append(state);
                    }
                    if (sb.length() > 0) {
                        sb.deleteCharAt(0);
                    }

                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put("eventTime", eventTime);
                    map.put("dataList", sb.toString());
                    accidentList.add(map);
                }
                message.setAccidentList(accidentList);
            }
            return message;
        } catch (Exception e) {
            throw new IllegalMessageException(e);
        }
    }
}
