package com.legaoyi.protocol.messagebody.decoder;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.legaoyi.protocol.exception.IllegalMessageException;
import com.legaoyi.protocol.message.MessageBody;
import com.legaoyi.protocol.message.decoder.MessageBodyDecoder;
import com.legaoyi.protocol.upstream.messagebody.Jt808_2019_0802_MessageBody;
import com.legaoyi.protocol.upstream.messagebody.Jt808_2019_0802_MessageBody.Multimedia;
import com.legaoyi.protocol.util.ByteUtils;

/**
 * @author <a href="mailto:shengbo.gao@gmail.com;78772895@qq.com">gaoshengbo</a>
 * @version 1.0.0
 * @since 2019-05-20
 */
@Component(MessageBodyDecoder.MESSAGE_BODY_DECODER_BEAN_PREFIX + "0802_2019" + MessageBodyDecoder.MESSAGE_BODY_DECODER_BEAN_SUFFIX)
public class Jt808_2019_0802_MessageBodyDecoder implements MessageBodyDecoder {

    @Override
    public MessageBody decode(byte[] bytes) throws IllegalMessageException {
        Jt808_2019_0802_MessageBody messageBody = new Jt808_2019_0802_MessageBody();
        try {
            int offset = 0;

            byte[] arr = new byte[2];
            System.arraycopy(bytes, offset, arr, 0, arr.length);
            messageBody.setMessageSeq(ByteUtils.word2int(arr));
            offset += arr.length;

            System.arraycopy(bytes, offset, arr, 0, arr.length);
            int itemCount = ByteUtils.word2int(arr);
            offset += arr.length;

            List<Jt808_2019_0802_MessageBody.Multimedia> itemList = new ArrayList<Jt808_2019_0802_MessageBody.Multimedia>();
            MessageBodyDecoder gpsInfoDecoder = new Jt808_2019_0200_MessageBodyDecoder();
            for (int i = 0; i < itemCount; i++) {
                arr = new byte[4];
                System.arraycopy(bytes, offset, arr, 0, arr.length);
                long mediaDataId = ByteUtils.dword2long(arr);
                offset += arr.length;

                int mediaType = ByteUtils.byte2int(bytes[offset++]);
                int channelId = ByteUtils.byte2int(bytes[offset++]);
                int mediaFormatCode = ByteUtils.byte2int(bytes[offset++]);

                arr = new byte[28];
                System.arraycopy(bytes, offset, arr, 0, arr.length);
                offset += arr.length;

                MessageBody gpsInfo = (MessageBody) gpsInfoDecoder.decode(arr);

                Multimedia multimedia = messageBody.new Multimedia();
                multimedia.setMediaDataId(mediaDataId);
                multimedia.setMediaType(mediaType);
                multimedia.setChannelId(channelId);
                multimedia.setMediaFormatCode(mediaFormatCode);
                multimedia.setGpsInfo(gpsInfo);
                itemList.add(multimedia);
            }
            messageBody.setItemList(itemList);
            return messageBody;
        } catch (Exception e) {
            throw new IllegalMessageException(e);
        }
    }
}
