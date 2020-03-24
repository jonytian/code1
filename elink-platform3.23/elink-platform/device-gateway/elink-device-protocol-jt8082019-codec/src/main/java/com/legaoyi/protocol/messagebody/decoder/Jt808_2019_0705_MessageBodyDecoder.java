package com.legaoyi.protocol.messagebody.decoder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.legaoyi.protocol.exception.IllegalMessageException;
import com.legaoyi.protocol.message.MessageBody;
import com.legaoyi.protocol.message.decoder.MessageBodyDecoder;
import com.legaoyi.protocol.upstream.messagebody.Jt808_2019_0705_MessageBody;
import com.legaoyi.protocol.util.ByteUtils;
import com.legaoyi.protocol.util.DateUtils;

/**
 * @author <a href="mailto:shengbo.gao@gmail.com;78772895@qq.com">gaoshengbo</a>
 * @version 1.0.0
 * @since 2019-05-20
 */
@Component(MessageBodyDecoder.MESSAGE_BODY_DECODER_BEAN_PREFIX + "0705_2019" + MessageBodyDecoder.MESSAGE_BODY_DECODER_BEAN_SUFFIX)
public class Jt808_2019_0705_MessageBodyDecoder implements MessageBodyDecoder {

    @Override
    public MessageBody decode(byte[] bytes) throws IllegalMessageException {
        Jt808_2019_0705_MessageBody messageBody = new Jt808_2019_0705_MessageBody();
        try {
            int offset = 0;

            byte[] arr = new byte[2];
            System.arraycopy(bytes, offset, arr, 0, arr.length);
            int count = ByteUtils.word2int(arr);
            offset += arr.length;

            arr = new byte[5];
            System.arraycopy(bytes, offset, arr, 0, arr.length);
            messageBody.setDataTime(DateUtils.bcd2dateTime(ByteUtils.bytes2bcd(arr)));
            offset += arr.length;

            List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
            for (int i = 0; i < count; i++) {
                Map<String, Object> data = new HashMap<String, Object>();
                arr = new byte[4];
                System.arraycopy(bytes, offset, arr, 0, arr.length);
                long canId = ByteUtils.dword2long(arr);
                offset += arr.length;
                data.put("canId", canId);

                arr = new byte[8];
                System.arraycopy(bytes, offset, arr, 0, arr.length);
                String canData = ByteUtils.bytes2hex(arr);
                offset += arr.length;
                data.put("canData", canData);
                dataList.add(data);
            }
            messageBody.setDataList(dataList);
            return messageBody;
        } catch (Exception e) {
            throw new IllegalMessageException(e);
        }
    }
}
