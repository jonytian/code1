package com.legaoyi.protocol.messagebody.encoder;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import com.legaoyi.protocol.down.messagebody.Tjsatl_2017_8103_MessageBody;
import com.legaoyi.protocol.exception.IllegalMessageException;
import com.legaoyi.protocol.message.MessageBody;
import com.legaoyi.protocol.message.encoder.MessageBodyEncoder;
import com.legaoyi.protocol.util.ByteUtils;
import com.legaoyi.protocol.util.MessageBuilder;

/**
 * 
 * @author <a href="mailto:shengbo.gao@gmail.com;78772895@qq.com">gaoshengbo</a>
 * @version 1.0.0
 * @since 2015-01-30
 */
@Component(MessageBodyEncoder.MESSAGE_BODY_ENCODER_BEAN_PREFIX + "8103_tjsatl" + MessageBodyEncoder.MESSAGE_BODY_ENCODER_BEAN_SUFFIX)
public class Tjsatl_2017_8103_MessageBodyEncoder implements MessageBodyEncoder {

    @Override
    public byte[] encode(MessageBody message) throws IllegalMessageException {
        try {
            Tjsatl_2017_8103_MessageBody messageBody = (Tjsatl_2017_8103_MessageBody) message;
            Map<String, String> paramList = messageBody.getParamList();
            MessageBuilder mb = new MessageBuilder();
            mb.addByte(paramList.size());
            for (String key : paramList.keySet()) {
                key = key.toUpperCase();
                String val = paramList.get(key);
                if ("F364".equals(key)) {
                    mb.append(ByteUtils.hex2dword(key));
                    append(mb, val.split(","), PARAM_TYPE_F364);
                } else if ("F365".equals(key)) {
                    mb.append(ByteUtils.hex2dword(key));
                    append(mb, val.split(","), PARAM_TYPE_F365);
                } else if ("F366".equals(key)) {
                    mb.append(ByteUtils.hex2dword(key));
                    String[] arr = val.split(",");
                    append(mb, arr, PARAM_TYPE_F366);
                } else if ("F367".equals(key)) {
                    mb.append(ByteUtils.hex2dword(key));
                    append(mb, val.split(","), PARAM_TYPE_F367);
                } else {
                    String type = PARAM_TYPE_MAP.get(key);
                    byte[] bytes = null;
                    if ("DWORD".equals(type)) {
                        bytes = ByteUtils.hex2dword(val);
                    } else if ("WORD".equals(type)) {
                        bytes = ByteUtils.hex2word(val);
                    } else if ("STRING".equals(type)) {
                        bytes = ByteUtils.gbk2bytes(val);
                    } else if ("BYTE".equals(type)) {
                        bytes = ByteUtils.hex2bytes(val);
                    } else {
                        continue;
                    }
                    mb.append(ByteUtils.hex2dword(key));
                    mb.addByte(bytes.length);
                    mb.append(bytes);
                }
            }
            return mb.getBytes();
        } catch (Exception e) {
            throw new IllegalMessageException(e);
        }
    }

    private void append(MessageBuilder mb, String[] values, String[] types) throws Exception {
        int index = 0;
        byte defaultByte = (byte) 0xff;

        MessageBuilder mb1 = new MessageBuilder();
        for (String s : values) {
            String type = types[index++];
            byte[] bytes = null;
            if ("DWORD".equals(type)) {
                if (StringUtils.isNotBlank(s)) {
                    bytes = ByteUtils.hex2dword(s);
                } else {
                    bytes = new byte[] {defaultByte, defaultByte, defaultByte, defaultByte};
                }
            } else if ("WORD".equals(type)) {
                if (StringUtils.isNotBlank(s)) {
                    bytes = ByteUtils.hex2word(s);
                } else {
                    bytes = new byte[] {defaultByte, defaultByte};
                }
            } else if ("BYTE".equals(type)) {
                if (StringUtils.isNotBlank(s)) {
                    bytes = ByteUtils.hex2bytes(s);
                } else {
                    bytes = new byte[] {defaultByte};
                }
            } else if (type.startsWith("BYTE[")) {
                int length = Integer.parseInt(type.substring(type.indexOf("[") + 1, type.indexOf("]")));
                if (StringUtils.isNotBlank(s)) {
                    bytes = new byte[length];
                } else {
                    bytes = ByteUtils.hex2bytes(s);
                    if (bytes.length < length) {
                        byte[] temp = new byte[length];
                        System.arraycopy(bytes, 0, temp, (length - bytes.length) - 1, bytes.length);
                        bytes = temp;
                    }
                }
            } else if (type.startsWith("STRING")) {
                int startIndex = type.indexOf("[");
                if (startIndex != -1) {
                    int length = Integer.parseInt(type.substring(startIndex + 1, type.indexOf("]")));
                    bytes = ByteUtils.ascii2bytes(s, length);
                } else {
                    bytes = ByteUtils.ascii2bytes(s);
                }
            }
            mb1.append(bytes);
        }

        byte[] bytes = mb1.getBytes();
        mb.addByte(bytes.length);
        mb.append(bytes);
    }

    // 高级驾驶辅助系统参数
    public static final String[] PARAM_TYPE_F364 = new String[] {"BYTE", "BYTE", "BYTE", "WORD", "WORD", "BYTE", "BYTE", "BYTE", "BYTE", "DWORD", "DWORD", "BYTE", "BYTE", "BYTE", "BYTE", "BYTE", "BYTE", "BYTE", "BYTE", "BYTE", "BYTE", "BYTE", "BYTE",
            "BYTE", "BYTE", "BYTE", "BYTE", "BYTE", "BYTE", "BYTE", "BYTE", "BYTE", "BYTE", "BYTE", "BYTE", "BYTE", "BYTE", "BYTE", "BYTE", "BYTE", "BYTE", "BYTE", "BYTE", "BYTE", "BYTE[4]"};

    // 驾驶员状态监测系统参数
    public static final String[] PARAM_TYPE_F365 = new String[] {"BYTE", "BYTE", "BYTE", "WORD", "WORD", "BYTE", "BYTE", "BYTE", "BYTE", "DWORD", "DWORD", "WORD", "WORD", "BYTE[3]", "BYTE", "BYTE", "BYTE", "BYTE", "BYTE", "BYTE", "BYTE", "BYTE",
            "BYTE", "BYTE", "BYTE", "BYTE", "BYTE", "BYTE", "BYTE", "BYTE", "BYTE", "BYTE", "BYTE", "BYTE", "BYTE", "BYTE[2]"};

    // 胎压监测系统参数
    public static final String[] PARAM_TYPE_F366 = new String[] {"STRING[12]", "WORD", "WORD", "WORD", "WORD", "WORD", "WORD", "WORD", "WORD", "WORD", "BYTE[6]"};

    // 盲区监测系统参数
    public static final String[] PARAM_TYPE_F367 = new String[] {"BYTE", "BYTE"};

    public static final Map<String, String[]> PARAM_TYPE_EXT_MAP = new HashMap<String, String[]>();
    static {
        PARAM_TYPE_EXT_MAP.put("F364", PARAM_TYPE_F364);
        PARAM_TYPE_EXT_MAP.put("F365", PARAM_TYPE_F365);
        PARAM_TYPE_EXT_MAP.put("F366", PARAM_TYPE_F366);
        PARAM_TYPE_EXT_MAP.put("F367", PARAM_TYPE_F367);
    }

    public static final Map<String, String> PARAM_TYPE_MAP = new HashMap<String, String>();
    static {
        PARAM_TYPE_MAP.put("0001", "DWORD");
        PARAM_TYPE_MAP.put("0002", "DWORD");
        PARAM_TYPE_MAP.put("0003", "DWORD");
        PARAM_TYPE_MAP.put("0004", "DWORD");
        PARAM_TYPE_MAP.put("0005", "DWORD");
        PARAM_TYPE_MAP.put("0006", "DWORD");
        PARAM_TYPE_MAP.put("0007", "DWORD");
        PARAM_TYPE_MAP.put("0010", "STRING");
        PARAM_TYPE_MAP.put("0011", "STRING");
        PARAM_TYPE_MAP.put("0012", "STRING");
        PARAM_TYPE_MAP.put("0013", "STRING");
        PARAM_TYPE_MAP.put("0014", "STRING");
        PARAM_TYPE_MAP.put("0015", "STRING");
        PARAM_TYPE_MAP.put("0016", "STRING");
        PARAM_TYPE_MAP.put("0017", "STRING");
        PARAM_TYPE_MAP.put("0018", "DWORD");
        PARAM_TYPE_MAP.put("0019", "DWORD");
        PARAM_TYPE_MAP.put("0020", "DWORD");
        PARAM_TYPE_MAP.put("0021", "DWORD");
        PARAM_TYPE_MAP.put("0022", "DWORD");
        PARAM_TYPE_MAP.put("0027", "DWORD");
        PARAM_TYPE_MAP.put("0028", "DWORD");
        PARAM_TYPE_MAP.put("0029", "DWORD");
        PARAM_TYPE_MAP.put("0030", "DWORD");
        PARAM_TYPE_MAP.put("002C", "DWORD");
        PARAM_TYPE_MAP.put("002D", "DWORD");
        PARAM_TYPE_MAP.put("002E", "DWORD");
        PARAM_TYPE_MAP.put("002F", "DWORD");
        PARAM_TYPE_MAP.put("0030", "DWORD");
        PARAM_TYPE_MAP.put("0031", "WORD");
        PARAM_TYPE_MAP.put("0040", "STRING");
        PARAM_TYPE_MAP.put("0041", "STRING");
        PARAM_TYPE_MAP.put("0042", "STRING");
        PARAM_TYPE_MAP.put("0043", "STRING");
        PARAM_TYPE_MAP.put("0044", "STRING");
        PARAM_TYPE_MAP.put("0045", "DWORD");
        PARAM_TYPE_MAP.put("0046", "DWORD");
        PARAM_TYPE_MAP.put("0047", "DWORD");
        PARAM_TYPE_MAP.put("0048", "STRING");
        PARAM_TYPE_MAP.put("0049", "STRING");
        PARAM_TYPE_MAP.put("0050", "DWORD");
        PARAM_TYPE_MAP.put("0051", "DWORD");
        PARAM_TYPE_MAP.put("0052", "DWORD");
        PARAM_TYPE_MAP.put("0053", "DWORD");
        PARAM_TYPE_MAP.put("0054", "DWORD");
        PARAM_TYPE_MAP.put("0055", "DWORD");
        PARAM_TYPE_MAP.put("0056", "DWORD");
        PARAM_TYPE_MAP.put("0057", "DWORD");
        PARAM_TYPE_MAP.put("0058", "DWORD");
        PARAM_TYPE_MAP.put("0059", "DWORD");
        PARAM_TYPE_MAP.put("005A", "DWORD");
        PARAM_TYPE_MAP.put("005B", "WORD");
        PARAM_TYPE_MAP.put("005C", "WORD");
        PARAM_TYPE_MAP.put("005D", "WORD");
        PARAM_TYPE_MAP.put("005E", "WORD");
        PARAM_TYPE_MAP.put("0064", "DWORD");
        PARAM_TYPE_MAP.put("0065", "DWORD");
        PARAM_TYPE_MAP.put("0070", "DWORD");
        PARAM_TYPE_MAP.put("0071", "DWORD");
        PARAM_TYPE_MAP.put("0072", "DWORD");
        PARAM_TYPE_MAP.put("0073", "DWORD");
        PARAM_TYPE_MAP.put("0074", "DWORD");
        PARAM_TYPE_MAP.put("0080", "DWORD");
        PARAM_TYPE_MAP.put("0081", "WORD");
        PARAM_TYPE_MAP.put("0082", "WORD");
        PARAM_TYPE_MAP.put("0083", "STRING");
        PARAM_TYPE_MAP.put("0084", "BYTE");
        PARAM_TYPE_MAP.put("0090", "BYTE");
        PARAM_TYPE_MAP.put("0091", "BYTE");
        PARAM_TYPE_MAP.put("0092", "BYTE");
        PARAM_TYPE_MAP.put("0093", "DWORD");
        PARAM_TYPE_MAP.put("0094", "BYTE");
        PARAM_TYPE_MAP.put("0095", "DWORD");
        PARAM_TYPE_MAP.put("0100", "DWORD");
        PARAM_TYPE_MAP.put("0101", "WORD");
        PARAM_TYPE_MAP.put("0102", "DWORD");
        PARAM_TYPE_MAP.put("0103", "WORD");
    }
}
