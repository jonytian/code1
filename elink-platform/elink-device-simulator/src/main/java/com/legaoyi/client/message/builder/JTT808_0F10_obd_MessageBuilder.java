package com.legaoyi.client.message.builder;

import java.util.Map;

import org.springframework.stereotype.Component;
import com.legaoyi.client.util.Constants;
import com.legaoyi.client.up.messagebody.JTT808_0F10_obd_MessageBody;
import com.legaoyi.protocol.message.MessageBody;
import com.legaoyi.protocol.util.ByteUtils;

@Component(Constants.MESSAGE_BUILDER_BEAN_PREFIX + "0F10_obd" + Constants.MESSAGE_BUILDER_BEAN_SUFFIX)
public class JTT808_0F10_obd_MessageBuilder implements MessageBuilder {

    @Override
    public MessageBody build(Map<String, Object> map) throws Exception {
        JTT808_0F10_obd_MessageBody messageBody = new JTT808_0F10_obd_MessageBody();
        messageBody.setGpsTime(System.currentTimeMillis());
        messageBody.setFlag(1);

        int count = getRandom(1, 100) % 5;
        if (count == 0) {
            return null;
        }

        String code = "";
        for (int i = 0; i < count; i++) {
            int p = getRandom(1, 100) % 2;
            code += "|P" + p + ByteUtils.bytes2hex(ByteUtils.int2word(getRandom(1, 200))).toUpperCase().replaceAll("0X", "").substring(0, 3);
        }
        messageBody.setCode(code.substring(1));
        return messageBody;
    }

    private static int getRandom(int start, int end) {
        return (int) (start + Math.random() * end);
    }

}
