package com.legaoyi.protocol.messagebody.decoder;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.legaoyi.protocol.exception.IllegalMessageException;
import com.legaoyi.protocol.message.MessageBody;
import com.legaoyi.protocol.message.decoder.MessageBodyDecoder;
import com.legaoyi.protocol.up.messagebody.JTT808_0802_MessageBody;
import com.legaoyi.protocol.up.messagebody.JTT808_0802_MessageBody.Multimedia;
import com.legaoyi.protocol.util.ByteUtils;

/**
 * @author <a href="mailto:shengbo.gao@gmail.com;78772895@qq.com">gaoshengbo</a>
 * @version 1.0.0
 * @since 2015-01-30
 */
@Component(MessageBodyDecoder.MESSAGE_BODY_DECODER_BEAN_PREFIX + "0802_2011" + MessageBodyDecoder.MESSAGE_BODY_DECODER_BEAN_SUFFIX)
public class JTT808_0802_MessageBodyDecoder implements MessageBodyDecoder {

    @Override
    public MessageBody decode(byte[] messageBody) throws IllegalMessageException {
        JTT808_0802_MessageBody message = new JTT808_0802_MessageBody();
        try {
            int offset = 0;
            byte[] arr = new byte[2];
            System.arraycopy(messageBody, offset, arr, 0, arr.length);
            message.setMessageSeq(ByteUtils.word2int(arr));
            offset += arr.length;

            arr = new byte[2];
            System.arraycopy(messageBody, offset, arr, 0, arr.length);
            int itemCount = ByteUtils.word2int(arr);
            offset += arr.length;

            List<JTT808_0802_MessageBody.Multimedia> itemList = new ArrayList<JTT808_0802_MessageBody.Multimedia>();
            MessageBodyDecoder gpsInfoDecoder = new JTT808_0200_MessageBodyDecoder();
            for (int i = 0; i < itemCount; i++) {
                arr = new byte[4];
                System.arraycopy(messageBody, offset, arr, 0, arr.length);
                Long mediaDataId = ByteUtils.dword2long(arr);
                offset += arr.length;

                int mediaType = ByteUtils.byte2int(messageBody[offset++]);
                int channelId = ByteUtils.byte2int(messageBody[offset++]);
                int mediaFormatCode = ByteUtils.byte2int(messageBody[offset++]);

                arr = new byte[28];
                System.arraycopy(messageBody, offset, arr, 0, arr.length);
                offset += arr.length;

                MessageBody gpsInfo = (MessageBody) gpsInfoDecoder.decode(arr);

                Multimedia multimedia = message.new Multimedia();
                multimedia.setMediaDataId(mediaDataId);
                multimedia.setMediaType(mediaType);
                multimedia.setChannelId(channelId);
                multimedia.setMediaFormatCode(mediaFormatCode);
                multimedia.setGpsInfo(gpsInfo);
                itemList.add(multimedia);
            }
            message.setItemList(itemList);
            return message;
        } catch (Exception e) {
            throw new IllegalMessageException(e);
        }
    }
}
