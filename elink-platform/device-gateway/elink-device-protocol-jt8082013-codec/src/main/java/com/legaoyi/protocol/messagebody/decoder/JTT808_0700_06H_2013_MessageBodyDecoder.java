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
import com.legaoyi.protocol.up.messagebody.JTT808_0700_06H_2013_MessageBody;
import com.legaoyi.protocol.util.ByteUtils;
import com.legaoyi.protocol.util.DateUtils;

/**
 * @author <a href="mailto:shengbo.gao@gmail.com;78772895@qq.com">gaoshengbo</a>
 * @version 1.0.0
 * @since 2015-01-30
 */
@Component(MessageBodyDecoder.MESSAGE_BODY_DECODER_BEAN_PREFIX + "0700_06H_2013" + MessageBodyDecoder.MESSAGE_BODY_DECODER_BEAN_SUFFIX)
public class JTT808_0700_06H_2013_MessageBodyDecoder extends JTT808_0700_2013_MessageBodyDecoder {

    @Override
    public MessageBody decode(byte[] messageBody) throws IllegalMessageException {
        JTT808_0700_06H_2013_MessageBody message = new JTT808_0700_06H_2013_MessageBody();
        try {
            int offset = 9;
            
            byte[] arr = new byte[6];
            System.arraycopy(messageBody, offset, arr, 0, arr.length);
            message.setRealTime(DateUtils.bcd2dateTime(ByteUtils.bytes2bcd(arr)));
            offset += arr.length;

            String state = StringUtils.reverse(ByteUtils.byte2bin(messageBody[offset]));
            offset++;

            List<Map<String, Object>> signal = new ArrayList<Map<String, Object>>();
            Map<String, Object> data;
            for (int i = 0; i < 8; i++) {
                arr = new byte[10];
                System.arraycopy(messageBody, offset, arr, 0, arr.length);
                offset += arr.length;
                String name = ByteUtils.bytes2gb2312(arr);
                data = new HashMap<String, Object>();
                data.put("state", state.charAt(i) == 0 ? "0" : "1");
                data.put("name", name);
                data.put("d", "D" + i);
                signal.add(data);
            }
            message.setSignal(signal);
            return message;
        } catch (Exception e) {
            throw new IllegalMessageException(e);
        }
    }
}
