package com.legaoyi.protocol.messagebody.decoder;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.legaoyi.protocol.exception.IllegalMessageException;
import com.legaoyi.protocol.message.MessageBody;
import com.legaoyi.protocol.message.decoder.MessageBodyDecoder;
import com.legaoyi.protocol.up.messagebody.JTT808_0705_2013_MessageBody;
import com.legaoyi.protocol.util.ByteUtils;
import com.legaoyi.protocol.util.DateUtils;

/**
 * @author <a href="mailto:shengbo.gao@gmail.com;78772895@qq.com">gaoshengbo</a>
 * @version 1.0.0
 * @since 2015-01-30
 */
@Component(MessageBodyDecoder.MESSAGE_BODY_DECODER_BEAN_PREFIX+"0705_2013"+MessageBodyDecoder.MESSAGE_BODY_DECODER_BEAN_SUFFIX)
public class JTT808_0705_2013_MessageBodyDecoder implements MessageBodyDecoder {

    @Override
    public MessageBody decode(byte[] messageBody) throws IllegalMessageException {
        JTT808_0705_2013_MessageBody message = new JTT808_0705_2013_MessageBody();
        try {
            int offset = 0;
            byte[] arr = new byte[2];
            System.arraycopy(messageBody, offset, arr, 0, arr.length);
            int count = ByteUtils.bytesToInt(arr);
            offset += arr.length;
            
            arr = new byte[5];
            System.arraycopy(messageBody, offset, arr, 0, arr.length);
            message.setDataTime(DateUtils.bcd2dateTime(ByteUtils.bytes2bcd(arr)));
            offset += arr.length;
            
            List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
            for(int i=0;i<count;i++){
                Map<String,Object> data = new HashMap<String, Object>();
                arr = new byte[4];
                System.arraycopy(messageBody, offset, arr, 0, arr.length);
                int canId = ByteUtils.bytesToInt(arr);
                offset += arr.length;
                data.put("canId", canId);
                
                arr = new byte[8];
                System.arraycopy(messageBody, offset, arr, 0, arr.length);
                String canData = ByteUtils.bytes2hex(arr);
                offset += arr.length;
                data.put("canData", canData);
                dataList.add(data);
            }
            message.setDataList(dataList);
            return message;
        } catch (Exception e) {
            throw new IllegalMessageException(e);
        }
    }
}
