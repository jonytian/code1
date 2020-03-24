package com.legaoyi.protocol.messagebody.encoder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.legaoyi.protocol.down.messagebody.JTT1078_8103_MessageBody;
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
@Component(MessageBodyEncoder.MESSAGE_BODY_ENCODER_BEAN_PREFIX + "8103_2016" + MessageBodyEncoder.MESSAGE_BODY_ENCODER_BEAN_SUFFIX)
public class JTT1078_8103_MessageBodyEncoder implements MessageBodyEncoder {

    public static final Map<String, String> paramTypeMap = new HashMap<String, String>();

    @Override
    public byte[] encode(MessageBody message) throws IllegalMessageException {
        try {
            JTT1078_8103_MessageBody messageBody = (JTT1078_8103_MessageBody) message;
            Map<String, Object> paramList = messageBody.getParamList();
            MessageBuilder mb = new MessageBuilder();
            mb.addByte(paramList.size());
            MessageBuilder mb1 = new MessageBuilder();
            for (String key : paramList.keySet()) {
                Object o = paramList.get(key);
                if (o instanceof String) {
                    String val = (String) o;
                    String type = paramTypeMap.get(key);
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
                } else {// 1078协议扩展
                    if ("0075".equals(key)) {
                        Map<?, ?> map = (Map<?, ?>) o;
                        mb1.append(ByteUtils.int2byte(Integer.valueOf(String.valueOf(map.get("rtsCoding")))));
                        mb1.append(ByteUtils.int2byte(Integer.valueOf(String.valueOf(map.get("rtsResolution")))));
                        mb1.append(ByteUtils.int2word(Integer.valueOf(String.valueOf(map.get("rtsKeyframeInterval")))));
                        mb1.append(ByteUtils.int2byte(Integer.valueOf(String.valueOf(map.get("rtsTargetFrameRate")))));
                        mb1.append(ByteUtils.int2dword(Integer.valueOf(String.valueOf(map.get("rtsTargetBitrate")))));
                        mb1.append(ByteUtils.int2byte(Integer.valueOf(String.valueOf(map.get("ramsCoding")))));
                        mb1.append(ByteUtils.int2byte(Integer.valueOf(String.valueOf(map.get("ramsResolution")))));
                        mb1.append(ByteUtils.int2word(Integer.valueOf(String.valueOf(map.get("ramsKeyframeInterval")))));
                        mb1.append(ByteUtils.int2byte(Integer.valueOf(String.valueOf(map.get("ramsTargetFrameRate")))));
                        mb1.append(ByteUtils.int2dword(Integer.valueOf(String.valueOf(map.get("ramsTargetBitrate")))));
                        mb1.append(ByteUtils.int2word(Integer.valueOf(String.valueOf(map.get("osdFlag")))));
                        mb1.append(ByteUtils.int2byte(Integer.valueOf(String.valueOf(map.get("enableAudio")))));
                    } else if ("0076".equals(key)) {
                        int l = 0, m = 0, n = 0;
                        List<?> list = (List<?>) o;
                        MessageBuilder mb2 = new MessageBuilder();
                        for (Object o1 : list) {
                            Map<?, ?> map = (Map<?, ?>) o1;
                            mb2.append(ByteUtils.int2byte(Integer.valueOf(String.valueOf(map.get("physicalChannel")))));
                            mb2.append(ByteUtils.int2byte(Integer.valueOf(String.valueOf(map.get("logicalChannel")))));
                            int channelType = Integer.valueOf(String.valueOf(map.get("channelType")));
                            mb2.append(ByteUtils.int2byte(channelType));
                            if (channelType == 0) {
                                l++;
                            } else if (channelType == 1) {
                                m++;
                            } else {
                                n++;
                            }
                            if (channelType != 1) {
                                mb2.append(ByteUtils.int2byte(Integer.valueOf(String.valueOf(map.get("connFlag")))));
                            }
                        }
                        mb1.addByte(l);
                        mb1.addByte(m);
                        mb1.addByte(n);
                        mb1.append(mb2.getBytes());
                    } else if ("0077".equals(key)) {
                        List<?> list = (List<?>) o;
                        if (list != null && !list.isEmpty()) {
                            mb1.addByte(list.size());
                            for (Object o1 : list) {
                                Map<?, ?> map = (Map<?, ?>) o1;
                                mb1.append(ByteUtils.int2byte(Integer.valueOf(String.valueOf(map.get("logicalChannel")))));
                                mb1.append(ByteUtils.int2byte(Integer.valueOf(String.valueOf(map.get("rtsCoding")))));
                                mb1.append(ByteUtils.int2byte(Integer.valueOf(String.valueOf(map.get("rtsResolution")))));
                                mb1.append(ByteUtils.int2word(Integer.valueOf(String.valueOf(map.get("rtsKeyframeInterval")))));
                                mb1.append(ByteUtils.int2byte(Integer.valueOf(String.valueOf(map.get("rtsTargetFrameRate")))));
                                mb1.append(ByteUtils.int2dword(Integer.valueOf(String.valueOf(map.get("rtsTargetBitrate")))));
                                mb1.append(ByteUtils.int2byte(Integer.valueOf(String.valueOf(map.get("ramsCoding")))));
                                mb1.append(ByteUtils.int2byte(Integer.valueOf(String.valueOf(map.get("ramsResolution")))));
                                mb1.append(ByteUtils.int2word(Integer.valueOf(String.valueOf(map.get("ramsKeyframeInterval")))));
                                mb1.append(ByteUtils.int2byte(Integer.valueOf(String.valueOf(map.get("ramsTargetFrameRate")))));
                                mb1.append(ByteUtils.int2dword(Integer.valueOf(String.valueOf(map.get("ramsTargetBitrate")))));
                                mb1.append(ByteUtils.int2word(Integer.valueOf(String.valueOf(map.get("osdFlag")))));
                            }
                        }
                    } else if ("0079".equals(key)) {
                        Map<?, ?> map = (Map<?, ?>) o;
                        mb1.append(ByteUtils.int2byte(Integer.valueOf(String.valueOf(map.get("threshold")))));
                        mb1.append(ByteUtils.int2byte(Integer.valueOf(String.valueOf(map.get("duration")))));
                        mb1.append(ByteUtils.int2byte(Integer.valueOf(String.valueOf(map.get("presetTime")))));
                    } else if ("007A".equals(key)) {
                        Map<?, ?> map = (Map<?, ?>) o;
                        mb1.append(ByteUtils.hex2dword(String.valueOf(map.get("flag"))));
                    } else if ("007B".equals(key)) {
                        Map<?, ?> map = (Map<?, ?>) o;
                        mb1.append(ByteUtils.int2byte(Integer.valueOf(String.valueOf(map.get("allowNum")))));
                        mb1.append(ByteUtils.int2byte(Integer.valueOf(String.valueOf(map.get("fatigueDrivingThreshold")))));
                    } else if ("007C".equals(key)) {
                        Map<?, ?> map = (Map<?, ?>) o;
                        mb1.append(ByteUtils.hex2bytes(String.valueOf(map.get("model"))));
                        mb1.append(ByteUtils.hex2bytes(String.valueOf(map.get("type"))));
                        mb1.append(ByteUtils.hex2bytes(String.valueOf(map.get("week"))));
                        mb1.append(ByteUtils.hex2bytes(String.valueOf(map.get("flag"))));
                        String time = String.valueOf(map.get("time1"));
                        String arr[] = time.split("-");
                        mb1.append(ByteUtils.bcd2bytes(arr[0].replace(":", ""), 2));
                        mb1.append(ByteUtils.bcd2bytes(arr[1].replace(":", ""), 2));
                        time = String.valueOf(map.get("time2"));
                        arr = time.split("-");
                        mb1.append(ByteUtils.bcd2bytes(arr[0].replace(":", ""), 2));
                        mb1.append(ByteUtils.bcd2bytes(arr[1].replace(":", ""), 2));
                        time = String.valueOf(map.get("time3"));
                        arr = time.split("-");
                        mb1.append(ByteUtils.bcd2bytes(arr[0].replace(":", ""), 2));
                        mb1.append(ByteUtils.bcd2bytes(arr[1].replace(":", ""), 2));
                        time = String.valueOf(map.get("time4"));
                        arr = time.split("-");
                        mb1.append(ByteUtils.bcd2bytes(arr[0].replace(":", ""), 2));
                        mb1.append(ByteUtils.bcd2bytes(arr[1].replace(":", ""), 2));
                    }
                    byte[] bytes = mb1.getBytes();
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

    static {
        paramTypeMap.put("0001", "DWORD");
        paramTypeMap.put("0002", "DWORD");
        paramTypeMap.put("0003", "DWORD");
        paramTypeMap.put("0004", "DWORD");
        paramTypeMap.put("0005", "DWORD");
        paramTypeMap.put("0006", "DWORD");
        paramTypeMap.put("0007", "DWORD");
        paramTypeMap.put("0010", "STRING");
        paramTypeMap.put("0011", "STRING");
        paramTypeMap.put("0012", "STRING");
        paramTypeMap.put("0013", "STRING");
        paramTypeMap.put("0014", "STRING");
        paramTypeMap.put("0015", "STRING");
        paramTypeMap.put("0016", "STRING");
        paramTypeMap.put("0017", "STRING");
        paramTypeMap.put("0018", "DWORD");
        paramTypeMap.put("0019", "DWORD");
        paramTypeMap.put("0020", "DWORD");
        paramTypeMap.put("0021", "DWORD");
        paramTypeMap.put("0022", "DWORD");
        paramTypeMap.put("0027", "DWORD");
        paramTypeMap.put("0028", "DWORD");
        paramTypeMap.put("0029", "DWORD");
        paramTypeMap.put("0030", "DWORD");
        paramTypeMap.put("002C", "DWORD");
        paramTypeMap.put("002D", "DWORD");
        paramTypeMap.put("002E", "DWORD");
        paramTypeMap.put("002F", "DWORD");
        paramTypeMap.put("0030", "DWORD");
        paramTypeMap.put("0031", "WORD");
        paramTypeMap.put("0040", "STRING");
        paramTypeMap.put("0041", "STRING");
        paramTypeMap.put("0042", "STRING");
        paramTypeMap.put("0043", "STRING");
        paramTypeMap.put("0044", "STRING");
        paramTypeMap.put("0045", "DWORD");
        paramTypeMap.put("0046", "DWORD");
        paramTypeMap.put("0047", "DWORD");
        paramTypeMap.put("0048", "STRING");
        paramTypeMap.put("0049", "STRING");
        paramTypeMap.put("0050", "DWORD");
        paramTypeMap.put("0051", "DWORD");
        paramTypeMap.put("0052", "DWORD");
        paramTypeMap.put("0053", "DWORD");
        paramTypeMap.put("0054", "DWORD");
        paramTypeMap.put("0055", "DWORD");
        paramTypeMap.put("0056", "DWORD");
        paramTypeMap.put("0057", "DWORD");
        paramTypeMap.put("0058", "DWORD");
        paramTypeMap.put("0059", "DWORD");
        paramTypeMap.put("005A", "DWORD");
        paramTypeMap.put("005B", "WORD");
        paramTypeMap.put("005C", "WORD");
        paramTypeMap.put("005D", "WORD");
        paramTypeMap.put("005E", "WORD");
        paramTypeMap.put("0064", "DWORD");
        paramTypeMap.put("0065", "DWORD");
        paramTypeMap.put("0070", "DWORD");
        paramTypeMap.put("0071", "DWORD");
        paramTypeMap.put("0072", "DWORD");
        paramTypeMap.put("0073", "DWORD");
        paramTypeMap.put("0074", "DWORD");
        paramTypeMap.put("0080", "DWORD");
        paramTypeMap.put("0081", "WORD");
        paramTypeMap.put("0082", "WORD");
        paramTypeMap.put("0083", "STRING");
        paramTypeMap.put("0084", "BYTE");
        paramTypeMap.put("0090", "BYTE");
        paramTypeMap.put("0091", "BYTE");
        paramTypeMap.put("0092", "BYTE");
        paramTypeMap.put("0093", "DWORD");
        paramTypeMap.put("0094", "BYTE");
        paramTypeMap.put("0095", "DWORD");
        paramTypeMap.put("0100", "DWORD");
        paramTypeMap.put("0101", "WORD");
        paramTypeMap.put("0102", "DWORD");
        paramTypeMap.put("0103", "WORD");
    }
}
