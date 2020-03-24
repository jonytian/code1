package com.legaoyi.protocol.messagebody.decoder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.legaoyi.protocol.exception.IllegalMessageException;
import com.legaoyi.protocol.message.MessageBody;
import com.legaoyi.protocol.message.decoder.MessageBodyDecoder;
import com.legaoyi.protocol.up.messagebody.Tjsatl_2017_1210_MessageBody;
import com.legaoyi.protocol.util.ByteUtils;
import com.legaoyi.protocol.util.DateUtils;

/**
 * @author <a href="mailto:shengbo.gao@gmail.com;78772895@qq.com">gaoshengbo</a>
 * @version 1.0.0
 * @since 2019-05-20
 */
@Component(MessageBodyDecoder.MESSAGE_BODY_DECODER_BEAN_PREFIX + "1210_tjsatl" + MessageBodyDecoder.MESSAGE_BODY_DECODER_BEAN_SUFFIX)
public class Tjsatl_2017_1210_MessageBodyDecoder implements MessageBodyDecoder {

    @Override
    public MessageBody decode(byte[] bytes) throws IllegalMessageException {
        Tjsatl_2017_1210_MessageBody messageBody = new Tjsatl_2017_1210_MessageBody();
        try {
            int offset = 0;
            byte[] arr = new byte[7];
            System.arraycopy(bytes, offset, arr, 0, arr.length);
            messageBody.setTerminalId(ByteUtils.bytes2ascii(bytes));
            offset += arr.length;

            // 协议有问题，重复了？
            arr = new byte[7];
            System.arraycopy(bytes, offset, arr, 0, arr.length);
            messageBody.setTerminalId(ByteUtils.bytes2ascii(bytes));
            offset += arr.length;

            arr = new byte[6];
            System.arraycopy(bytes, offset, arr, 0, arr.length);
            messageBody.setAlarmTime(DateUtils.bcd2dateTime(ByteUtils.bytes2bcd(arr)));
            offset += arr.length;

            messageBody.setAlarmSeq(ByteUtils.byte2int(bytes[offset++]));
            messageBody.setTotalFile(ByteUtils.byte2int(bytes[offset++]));
            messageBody.setAlarmExt(ByteUtils.byte2int(bytes[offset++]));

            arr = new byte[32];
            System.arraycopy(bytes, offset, arr, 0, arr.length);
            messageBody.setAlarmId(ByteUtils.bytes2ascii(bytes));
            offset += arr.length;

            messageBody.setType(ByteUtils.byte2int(bytes[offset++]));

            int total = ByteUtils.byte2int(bytes[offset++]);
            List<Map<String, Object>> fileList = new ArrayList<Map<String, Object>>();
            Map<String, Object> map;
            for (int i = 0; i < total; i++) {
                map = new HashMap<String, Object>();
                int length = ByteUtils.byte2int(bytes[offset++]);
                arr = new byte[length];
                System.arraycopy(bytes, offset, arr, 0, arr.length);
                map.put("name", ByteUtils.bytes2gbk(arr));
                offset += arr.length;

                arr = new byte[4];
                System.arraycopy(bytes, offset, arr, 0, arr.length);
                map.put("size", ByteUtils.dword2long(arr));
                offset += arr.length;

                fileList.add(map);
            }
            messageBody.setFileList(fileList);
            return messageBody;
        } catch (Exception e) {
            throw new IllegalMessageException(e);
        }
    }
}
