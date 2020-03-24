package com.legaoyi.client.messagebody.decoder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;
import com.legaoyi.client.down.messagebody.Tjsatl_2017_9212_MessageBody;
import com.legaoyi.protocol.exception.IllegalMessageException;
import com.legaoyi.protocol.message.MessageBody;
import com.legaoyi.protocol.message.decoder.MessageBodyDecoder;
import com.legaoyi.protocol.util.ByteUtils;

/**
 * 
 * @author <a href="mailto:shengbo.gao@gmail.com;78772895@qq.com">gaoshengbo</a>
 * @version 1.0.0
 * @since 2018-08-07
 */
@Component(MessageBodyDecoder.MESSAGE_BODY_DECODER_BEAN_PREFIX + "9212_tjsatl" + MessageBodyDecoder.MESSAGE_BODY_DECODER_BEAN_SUFFIX)
public class Tjsatl_2017_9212_MessageBodyDecoder implements MessageBodyDecoder {

    @Override
    public MessageBody decode(byte[] messageBody) throws IllegalMessageException {
        Tjsatl_2017_9212_MessageBody body = new Tjsatl_2017_9212_MessageBody();
        try {
            int offset = 0;
            int length = ByteUtils.byte2int(messageBody[offset++]);

            byte[] arr = new byte[length];
            System.arraycopy(messageBody, offset, arr, 0, arr.length);
            body.setFileName(ByteUtils.bytes2gbk(arr));
            offset += arr.length;

            body.setFileType(ByteUtils.byte2int(messageBody[offset++]));
            body.setResult(ByteUtils.byte2int(messageBody[offset++]));

            if (body.getResult() != 0) {
                int count = ByteUtils.byte2int(messageBody[offset++]);

                List<Map<String, Long>> packageList = new ArrayList<Map<String, Long>>();
                for (int i = 0; i < count; i++) {
                    Map<String, Long> map = new HashMap<String, Long>();

                    arr = new byte[4];
                    System.arraycopy(messageBody, offset, arr, 0, arr.length);
                    map.put("offset", ByteUtils.dword2long(arr));
                    offset += arr.length;

                    arr = new byte[4];
                    System.arraycopy(messageBody, offset, arr, 0, arr.length);
                    map.put("length", ByteUtils.dword2long(arr));
                    offset += arr.length;

                    packageList.add(map);
                }
                body.setPackageList(packageList);
            }
        } catch (Exception e) {
            throw new IllegalMessageException(e);
        }
        return body;
    }
}
