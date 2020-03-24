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
import com.legaoyi.protocol.upstream.messagebody.Jt808_2019_0700_06H_MessageBody;
import com.legaoyi.protocol.util.ByteUtils;
import com.legaoyi.protocol.util.DateUtils;

/**
 * @author <a href="mailto:shengbo.gao@gmail.com;78772895@qq.com">gaoshengbo</a>
 * @version 1.0.0
 * @since 2019-05-20
 */
@Component(MessageBodyDecoder.MESSAGE_BODY_DECODER_BEAN_PREFIX + "0700_06H_2019" + MessageBodyDecoder.MESSAGE_BODY_DECODER_BEAN_SUFFIX)
public class Jt808_2019_0700_06H_MessageBodyDecoder extends Jt808_2019_0700_MessageBodyDecoder {

    @Override
    public MessageBody decode(byte[] bytes) throws IllegalMessageException {
        Jt808_2019_0700_06H_MessageBody messageBody = new Jt808_2019_0700_06H_MessageBody();
        try {

            int offset = 9;

            byte[] arr = new byte[6];
            System.arraycopy(bytes, offset, arr, 0, arr.length);
            messageBody.setRealTime(DateUtils.bcd2dateTime(ByteUtils.bytes2bcd(arr)));
            offset += arr.length;

            String state = StringUtils.reverse(ByteUtils.byte2bin(bytes[offset]));
            offset++;

            List<Map<String, Object>> signal = new ArrayList<Map<String, Object>>();
            Map<String, Object> data;
            for (int i = 0; i < 8; i++) {
                arr = new byte[10];
                System.arraycopy(bytes, offset, arr, 0, arr.length);
                offset += arr.length;
                String name = ByteUtils.bytes2gb2312(arr);
                data = new HashMap<String, Object>();
                data.put("state", state.charAt(i) == 0 ? "0" : "1");
                data.put("name", name);
                data.put("d", "D" + i);
                signal.add(data);
            }
            messageBody.setSignal(signal);

            return messageBody;
        } catch (Exception e) {
            throw new IllegalMessageException(e);
        }
    }
}
