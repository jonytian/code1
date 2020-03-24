package com.legaoyi.protocol.messagebody.decoder;

import org.springframework.stereotype.Component;

import com.legaoyi.protocol.exception.IllegalMessageException;
import com.legaoyi.protocol.message.MessageBody;
import com.legaoyi.protocol.message.decoder.MessageBodyDecoder;
import com.legaoyi.protocol.up.messagebody.JTT1078_1003_MessageBody;
import com.legaoyi.protocol.util.ByteUtils;

/**
 * @author <a href="mailto:shengbo.gao@gmail.com;78772895@qq.com">gaoshengbo</a>
 * @version 1.0.0
 * @since  2018-04-09
 */
@Component(MessageBodyDecoder.MESSAGE_BODY_DECODER_BEAN_PREFIX+"1003_2016"+MessageBodyDecoder.MESSAGE_BODY_DECODER_BEAN_SUFFIX)
public class JTT1078_1003_MessageBodyDecoder implements MessageBodyDecoder {

    @Override
    public MessageBody decode(byte[] messageBody) throws IllegalMessageException {
        JTT1078_1003_MessageBody message = new JTT1078_1003_MessageBody();
        try {
            int offset = 0;
            byte[] arr = new byte[1];
            System.arraycopy(messageBody, offset, arr, 0, arr.length);
            message.setAudioCoding(ByteUtils.byte2int(arr[0]));

            offset += arr.length;
            arr = new byte[1];
            System.arraycopy(messageBody, offset, arr, 0, arr.length);
            message.setInputChlNum(ByteUtils.byte2int(arr[0]));

            offset += arr.length;
            arr = new byte[1];
            System.arraycopy(messageBody, offset, arr, 0, arr.length);
            message.setSamplingRate(ByteUtils.byte2int(arr[0]));

            offset += arr.length;
            arr = new byte[1];
            System.arraycopy(messageBody, offset, arr, 0, arr.length);
            message.setSamplingNum(ByteUtils.byte2int(arr[0]));

            offset += arr.length;
            arr = new byte[2];
            System.arraycopy(messageBody, offset, arr, 0, arr.length);
            message.setFrameLen(ByteUtils.word2int(arr));

            offset += arr.length;
            arr = new byte[1];
            System.arraycopy(messageBody, offset, arr, 0, arr.length);
            message.setOutput(ByteUtils.byte2int(arr[0])==0?false:true);

            offset += arr.length;
            arr = new byte[1];
            System.arraycopy(messageBody, offset, arr, 0, arr.length);
            message.setVideoCoding(ByteUtils.byte2int(arr[0]));
            
            offset += arr.length;
            arr = new byte[1];
            System.arraycopy(messageBody, offset, arr, 0, arr.length);
            message.setMaxAudioChlNum(ByteUtils.byte2int(arr[0]));

            offset += arr.length;
            arr = new byte[1];
            System.arraycopy(messageBody, offset, arr, 0, arr.length);
            message.setMaxVideoChlNum(ByteUtils.byte2int(arr[0]));

            return message;
        } catch (Exception e) {
            throw new IllegalMessageException(e);
        }
    }
}
