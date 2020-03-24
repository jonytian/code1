package com.legaoyi.client.messagebody.encoder;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.legaoyi.protocol.exception.IllegalMessageException;
import com.legaoyi.protocol.message.MessageBody;
import com.legaoyi.protocol.message.encoder.MessageBodyEncoder;
import com.legaoyi.client.up.messagebody.Tjsatl_2017_0200_MessageBody;
import com.legaoyi.protocol.util.ByteUtils;
import com.legaoyi.protocol.util.DateUtils;
import com.legaoyi.protocol.util.MessageBuilder;

/**
 * @author <a href="mailto:shengbo.gao@gmail.com;78772895@qq.com">gaoshengbo</a>
 * @version 1.0.0
 * @since 2018-08-07
 */
@Component(MessageBodyEncoder.MESSAGE_BODY_ENCODER_BEAN_PREFIX + "0200_tjsatl" + MessageBodyEncoder.MESSAGE_BODY_ENCODER_BEAN_SUFFIX)
public class Tjsatl_2017_0200_MessageBodyEncoder implements MessageBodyEncoder {

    @SuppressWarnings("unchecked")
    @Override
    public byte[] encode(MessageBody message) throws IllegalMessageException {
        try {
            Tjsatl_2017_0200_MessageBody messageBody = (Tjsatl_2017_0200_MessageBody) message;
            MessageBuilder mb = new MessageBuilder();
            mb.addDword((int) messageBody.getAlarm());
            mb.addDword((int) messageBody.getState());
            mb.addDword((int) (messageBody.getLat() * 1000000));
            mb.addDword((int) (messageBody.getLng() * 1000000));
            mb.addWord(messageBody.getAltitude());
            mb.addWord((int) (messageBody.getSpeed() * 10));
            mb.addWord(messageBody.getDirection());
            mb.append(ByteUtils.bcd2bytes(DateUtils.timestamp2bcd(messageBody.getGpsTime()), 6));

            mb.append((byte) 0x01);
            mb.addByte(4);
            mb.addDword((int) (messageBody.getMileage() * 10));

            mb.append((byte) 0x02);
            mb.addByte(2);
            mb.addWord((int) (messageBody.getOilmass() * 10));

            mb.append((byte) 0x03);
            mb.addByte(2);
            mb.addWord((int) (messageBody.getDvrSpeed() * 10));

            /// *******************
            MessageBuilder mb1 = new MessageBuilder();
            Map<String, Object> adasAlarm = messageBody.getAdasAlarm();
            if (adasAlarm != null) {
                mb.append((byte) 0x64);

                mb1.addDword((int) adasAlarm.get("alarmId"));
                mb1.addByte((int) adasAlarm.get("flag"));
                int type = (int) adasAlarm.get("type");
                mb1.addByte(type);
                mb1.addByte((int) adasAlarm.get("level"));
                // if (type < 0x03) {
                mb1.addByte((int) adasAlarm.get("frontSpeed"));
                // }
                // if (type < 0x03 || type == 0x04) {
                mb1.addByte((int) adasAlarm.get("frontDistance"));
                // }
                // if (type == 0x02) {
                mb1.addByte((int) adasAlarm.get("deviationType"));
                // }
                // if (type == 0x06 || type == 0x10) {
                mb1.addByte((int) adasAlarm.get("roadSignType"));
                mb1.addByte((int) adasAlarm.get("roadSignData"));
                // }
                mb1.addByte((int) adasAlarm.get("speed"));
                mb1.addWord((int) adasAlarm.get("altitude"));
                mb1.addDword((int) (messageBody.getLat() * 1000000));
                mb1.addDword((int) (messageBody.getLng() * 1000000));
                mb1.append(ByteUtils.bcd2bytes(DateUtils.timestamp2bcd(messageBody.getGpsTime()), 6));
                mb1.addWord((int) messageBody.getState());
                mb1.append(ByteUtils.ascii2bytes((String) adasAlarm.get("terminalId"), 7));
                mb1.append(ByteUtils.bcd2bytes(DateUtils.timestamp2bcd(messageBody.getGpsTime()), 6));
                mb1.addByte((int) adasAlarm.get("alarmSeq"));
                mb1.addByte((int) adasAlarm.get("totalFile"));
                mb1.addByte((int) adasAlarm.get("alarmExt"));

                byte[] bytes = mb1.getBytes();
                mb.addByte(bytes.length);
                mb.append(bytes);
            }
            /// *****************

            /// *******************

            Map<String, Object> dsmAlarm = messageBody.getDsmAlarm();
            if (dsmAlarm != null) {
                mb.append((byte) 0x65);
                mb1 = new MessageBuilder();
                mb1.addDword((int) dsmAlarm.get("alarmId"));
                mb1.addByte((int) dsmAlarm.get("flag"));
                mb1.addByte((int) dsmAlarm.get("type"));
                mb1.addByte((int) dsmAlarm.get("level"));
                mb1.addByte((int) dsmAlarm.get("fatigueDegree"));
                mb1.addDword((int) dsmAlarm.get("ext"));
                mb1.addByte((int) dsmAlarm.get("speed"));
                mb1.addWord((int) dsmAlarm.get("altitude"));
                mb1.addDword((int) (messageBody.getLat() * 1000000));
                mb1.addDword((int) (messageBody.getLng() * 1000000));
                mb1.append(ByteUtils.bcd2bytes(DateUtils.timestamp2bcd(messageBody.getGpsTime()), 6));
                mb1.addWord((int) messageBody.getState());
                mb1.append(ByteUtils.ascii2bytes((String) dsmAlarm.get("terminalId"), 7));
                mb1.append(ByteUtils.bcd2bytes(DateUtils.timestamp2bcd(messageBody.getGpsTime()), 6));
                mb1.addByte((int) dsmAlarm.get("alarmSeq"));
                mb1.addByte((int) dsmAlarm.get("totalFile"));
                mb1.addByte((int) dsmAlarm.get("alarmExt"));

                byte[] bytes = mb1.getBytes();
                mb.addByte(bytes.length);
                mb.append(bytes);
            }
            /// *****************

            /// *******************
            Map<String, Object> tpmAlarm = messageBody.getTpmAlarm();
            if (tpmAlarm != null) {
                mb.append((byte) 0x66);
                mb1 = new MessageBuilder();
                mb1.addDword((int) tpmAlarm.get("alarmId"));
                mb1.addByte((int) tpmAlarm.get("flag"));
                mb1.addByte((int) tpmAlarm.get("speed"));
                mb1.addWord((int) tpmAlarm.get("altitude"));
                mb1.addDword((int) (messageBody.getLat() * 1000000));
                mb1.addDword((int) (messageBody.getLng() * 1000000));
                mb1.append(ByteUtils.bcd2bytes(DateUtils.timestamp2bcd(messageBody.getGpsTime()), 6));
                mb1.addWord((int) messageBody.getState());
                mb1.append(ByteUtils.ascii2bytes((String) tpmAlarm.get("terminalId"), 7));
                mb1.append(ByteUtils.bcd2bytes(DateUtils.timestamp2bcd(messageBody.getGpsTime()), 6));
                mb1.addByte((int) tpmAlarm.get("alarmSeq"));
                mb1.addByte((int) tpmAlarm.get("totalFile"));
                mb1.addByte((int) tpmAlarm.get("alarmExt"));
                List<Map<String, Object>> alarmList = (List<Map<String, Object>>) tpmAlarm.get("alarmList");
                mb1.addByte(alarmList.size());
                for (Map<String, Object> map : alarmList) {
                    mb1.addByte((int) map.get("seq"));
                    mb1.addWord((int) map.get("type"));
                    mb1.addWord((int) map.get("pressure"));
                    mb1.addWord((int) map.get("temperature"));
                    mb1.addWord((int) map.get("electricity"));
                }

                byte[] bytes = mb1.getBytes();
                mb.addByte(bytes.length);
                mb.append(bytes);
            }
            /// *****************

            /// *******************
            Map<String, Object> bsdAlarm = messageBody.getBsdAlarm();
            if (bsdAlarm != null) {
                mb.append((byte) 0x67);
                mb1 = new MessageBuilder();

                mb1.addDword((int) bsdAlarm.get("alarmId"));
                mb1.addByte((int) bsdAlarm.get("flag"));
                mb1.addByte((int) bsdAlarm.get("type"));
                mb1.addByte((int) bsdAlarm.get("speed"));
                mb1.addWord((int) bsdAlarm.get("altitude"));
                mb1.addDword((int) (messageBody.getLat() * 1000000));
                mb1.addDword((int) (messageBody.getLng() * 1000000));
                mb1.append(ByteUtils.bcd2bytes(DateUtils.timestamp2bcd(messageBody.getGpsTime()), 6));
                mb1.addWord((int) messageBody.getState());
                mb1.append(ByteUtils.ascii2bytes((String) bsdAlarm.get("terminalId"), 7));
                mb1.append(ByteUtils.bcd2bytes(DateUtils.timestamp2bcd(messageBody.getGpsTime()), 6));
                mb1.addByte((int) bsdAlarm.get("alarmSeq"));
                mb1.addByte((int) bsdAlarm.get("totalFile"));
                mb1.addByte((int) bsdAlarm.get("alarmExt"));

                byte[] bytes = mb1.getBytes();
                mb.addByte(bytes.length);
                mb.append(bytes);
            }
            /// *****************

            return mb.getBytes();
        } catch (Exception e) {
            throw new IllegalMessageException(e);
        }
    }
}
