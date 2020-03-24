package com.legaoyi.protocol.messagebody.decoder;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.legaoyi.protocol.exception.IllegalMessageException;
import com.legaoyi.protocol.message.MessageBody;
import com.legaoyi.protocol.message.decoder.MessageBodyDecoder;
import com.legaoyi.protocol.messagebody.encoder.Tjsatl_2017_8103_MessageBodyEncoder;
import com.legaoyi.protocol.up.messagebody.Tjsatl_2017_0104_MessageBody;
import com.legaoyi.protocol.util.ByteUtils;

/**
 * @author <a href="mailto:shengbo.gao@gmail.com;78772895@qq.com">gaoshengbo</a>
 * @version 1.0.0
 * @since 2019-05-20
 */
@Component(MessageBodyDecoder.MESSAGE_BODY_DECODER_BEAN_PREFIX + "0104_tjsatl" + MessageBodyDecoder.MESSAGE_BODY_DECODER_BEAN_SUFFIX)
public class Tjsatl_2017_0104_MessageBodyDecoder implements MessageBodyDecoder {

    @Override
    public MessageBody decode(byte[] bytes) throws IllegalMessageException {
        Tjsatl_2017_0104_MessageBody messageBody = new Tjsatl_2017_0104_MessageBody();
        try {
            int offset = 0;

            byte[] arr = new byte[2];
            System.arraycopy(bytes, offset, arr, 0, arr.length);
            messageBody.setMessageSeq(ByteUtils.word2int(arr));
            offset += arr.length;

            int count = ByteUtils.byte2int(bytes[offset]);
            offset++;

            Map<String, String> paramList = new HashMap<String, String>();
            for (int i = 0; i < count; i++) {
                arr = new byte[4];
                System.arraycopy(bytes, offset, arr, 0, arr.length);
                String key = ByteUtils.bytes2hex(arr);
                offset += arr.length;
                int dataLen = ByteUtils.byte2int(bytes[offset++]);

                key = key.substring(4);
                String val = null;
                if (key.startsWith("F36")) {
                    StringBuilder sb = new StringBuilder();
                    if (key.equals("F366")) {
                        arr = new byte[12];
                        System.arraycopy(bytes, offset, arr, 0, arr.length);
                        sb.append(",").append(ByteUtils.bytes2ascii(arr));
                        offset += arr.length;
                    }

                    String[] types = Tjsatl_2017_8103_MessageBodyEncoder.PARAM_TYPE_EXT_MAP.get(key);
                    for (int j = 0; j < types.length; j++) {
                        String type = types[j];
                        if ("DWORD".equals(type)) {
                            arr = new byte[4];
                        } else if ("WORD".equals(type)) {
                            arr = new byte[2];
                        } else if ("BYTE".equals(type)) {
                            arr = new byte[1];
                        } else if ("BYTE[".indexOf(type) != -1) {
                            int length = Integer.parseInt(type.substring(type.indexOf("["), type.indexOf("]")));
                            arr = new byte[length];
                        } else {
                            continue;
                        }
                        System.arraycopy(bytes, offset, arr, 0, arr.length);
                        sb.append(",").append(ByteUtils.bytes2hex(arr));
                        offset += arr.length;
                    }
                    if (count > 0) {
                        sb.delete(0, 1);
                        val = sb.toString();
                    }
                } else {
                    String type = Tjsatl_2017_8103_MessageBodyEncoder.PARAM_TYPE_MAP.get(key);
                    if ("DWORD".equals(type)) {
                        arr = new byte[4];
                        System.arraycopy(bytes, offset, arr, 0, arr.length);
                        val = ByteUtils.bytes2hex(arr);
                        offset += arr.length;
                    } else if ("WORD".equals(type)) {
                        arr = new byte[2];
                        System.arraycopy(bytes, offset, arr, 0, arr.length);
                        val = ByteUtils.bytes2hex(arr);
                        offset += arr.length;
                    } else if ("STRING".equals(type)) {
                        arr = new byte[dataLen];
                        System.arraycopy(bytes, offset, arr, 0, arr.length);
                        val = ByteUtils.bytes2gbk(arr);
                        offset += arr.length;
                    } else if ("BYTE".equals(type)) {
                        val = ByteUtils.byte2hex(bytes[offset]);
                        offset++;
                    } else {
                        continue;
                    }
                }
                paramList.put(key, val);
            }
            messageBody.setParamList(paramList);
            return messageBody;
        } catch (Exception e) {
            throw new IllegalMessageException(e);
        }
    }
}
