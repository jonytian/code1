package com.legaoyi.protocol.messagebody.decoder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.legaoyi.protocol.exception.IllegalMessageException;
import com.legaoyi.protocol.message.MessageBody;
import com.legaoyi.protocol.message.decoder.MessageBodyDecoder;
import com.legaoyi.protocol.up.messagebody.Tjsatl_2017_0200_MessageBody;
import com.legaoyi.protocol.util.ByteUtils;
import com.legaoyi.protocol.util.DateUtils;

/**
 * @author <a href="mailto:shengbo.gao@gmail.com;78772895@qq.com">gaoshengbo</a>
 * @version 1.0.0
 * @since 2015-01-30
 */
@Component(MessageBodyDecoder.MESSAGE_BODY_DECODER_BEAN_PREFIX + "0200_tjsatl" + MessageBodyDecoder.MESSAGE_BODY_DECODER_BEAN_SUFFIX)
public class Tjsatl_2017_0200_MessageBodyDecoder implements MessageBodyDecoder {

    @Override
    public MessageBody decode(byte[] messageBody) throws IllegalMessageException {
        Tjsatl_2017_0200_MessageBody message = new Tjsatl_2017_0200_MessageBody();
        try {
            int offset = 0;
            byte[] arr = new byte[4];
            System.arraycopy(messageBody, offset, arr, 0, arr.length);
            message.setAlarm(ByteUtils.dword2long(arr));

            offset += arr.length;
            System.arraycopy(messageBody, offset, arr, 0, arr.length);
            message.setState(ByteUtils.dword2long(arr));

            offset += arr.length;
            System.arraycopy(messageBody, offset, arr, 0, arr.length);
            message.setLat(String.valueOf(Double.valueOf(Double.valueOf(ByteUtils.dword2long(arr)).doubleValue() / 1000000.0D)));

            offset += arr.length;
            System.arraycopy(messageBody, offset, arr, 0, arr.length);
            message.setLng(String.valueOf(Double.valueOf(Double.valueOf(ByteUtils.dword2long(arr)).doubleValue() / 1000000.0D)));

            offset += arr.length;
            arr = new byte[2];
            System.arraycopy(messageBody, offset, arr, 0, arr.length);
            int altitude = ByteUtils.word2int(arr);
            if (altitude > 32767) {
                altitude -= 65536;
            }
            message.setAltitude(altitude);

            offset += arr.length;
            System.arraycopy(messageBody, offset, arr, 0, arr.length);
            double speed = ByteUtils.word2int(arr);
            if (speed > 0) {
                speed = speed / 10.0d;
            }
            message.setSpeed(speed);

            offset += arr.length;
            System.arraycopy(messageBody, offset, arr, 0, arr.length);
            message.setDirection(ByteUtils.word2int(arr));

            offset += arr.length;
            arr = new byte[6];
            System.arraycopy(messageBody, offset, arr, 0, arr.length);
            message.setGpsTime(DateUtils.bcd2Timestamp(ByteUtils.bytes2bcd(arr)));

            offset += arr.length;
            while (offset < messageBody.length) {
                byte param = messageBody[offset++];

                int length = ByteUtils.byte2int(messageBody[offset++]);

                if (param == 0x01) {
                    arr = new byte[4];
                    System.arraycopy(messageBody, offset, arr, 0, arr.length);
                    double mileage = ByteUtils.dword2long(arr);
                    if (mileage > 0) {
                        mileage = mileage / 10.0d;
                    }
                    message.setMileage(mileage);
                    offset += arr.length;
                } else if (param == 0x02) {
                    arr = new byte[2];
                    System.arraycopy(messageBody, offset, arr, 0, arr.length);
                    double oilmass = ByteUtils.word2int(arr);
                    if (oilmass > 0) {
                        oilmass = oilmass / 10.0d;
                    }
                    message.setOilmass(oilmass);
                    offset += arr.length;
                } else if (param == 0x03) {
                    arr = new byte[2];
                    System.arraycopy(messageBody, offset, arr, 0, arr.length);
                    double dvrSpeed = ByteUtils.word2int(arr);
                    if (dvrSpeed > 0) {
                        dvrSpeed = dvrSpeed / 10.0d;
                    }
                    message.setDvrSpeed(dvrSpeed);
                    offset += arr.length;
                } else if (param == 0x04) {
                    arr = new byte[2];
                    System.arraycopy(messageBody, offset, arr, 0, arr.length);
                    message.setAlarmEventId(ByteUtils.word2int(arr));
                    offset += arr.length;
                } else if (param == 0x11) {
                    Map<String, Object> overSpeedAlarmExt = new HashMap<String, Object>();
                    arr = new byte[1];
                    System.arraycopy(messageBody, offset, arr, 0, arr.length);
                    overSpeedAlarmExt.put("type", ByteUtils.byte2int(arr[0]));
                    offset += arr.length;
                    if (length > 1) {
                        arr = new byte[4];
                        System.arraycopy(messageBody, offset, arr, 0, arr.length);
                        overSpeedAlarmExt.put("id", ByteUtils.dword2long(arr));
                        offset += arr.length;
                    }
                    message.setOverSpeedAlarmExt(overSpeedAlarmExt);
                } else if (param == 0x12) {
                    Map<String, Object> inOutAlarmExt = new HashMap<String, Object>();
                    arr = new byte[1];
                    System.arraycopy(messageBody, offset, arr, 0, arr.length);
                    inOutAlarmExt.put("type", ByteUtils.byte2int(arr[0]));
                    offset += arr.length;

                    arr = new byte[4];
                    System.arraycopy(messageBody, offset, arr, 0, arr.length);
                    inOutAlarmExt.put("id", ByteUtils.dword2long(arr));
                    offset += arr.length;

                    arr = new byte[1];
                    System.arraycopy(messageBody, offset, arr, 0, arr.length);
                    inOutAlarmExt.put("direction", ByteUtils.byte2int(arr[0]));

                    message.setInOutAlarmExt(inOutAlarmExt);
                    offset += arr.length;
                } else if (param == 0x13) {
                    Map<String, Object> runningTimeAlarmExt = new HashMap<String, Object>();
                    arr = new byte[4];
                    System.arraycopy(messageBody, offset, arr, 0, arr.length);
                    runningTimeAlarmExt.put("id", ByteUtils.dword2long(arr));
                    offset += arr.length;

                    arr = new byte[2];
                    System.arraycopy(messageBody, offset, arr, 0, arr.length);
                    runningTimeAlarmExt.put("runTime", ByteUtils.word2int(arr));
                    offset += arr.length;

                    arr = new byte[1];
                    System.arraycopy(messageBody, offset, arr, 0, arr.length);
                    runningTimeAlarmExt.put("result", ByteUtils.byte2int(arr[0]));

                    message.setRunningTimeAlarmExt(runningTimeAlarmExt);
                    offset += arr.length;
                }
                // *******tjsatl协议 start********//
                else if (param == 0x64) {// 高级驾驶辅助系统报警信息
                    arr = new byte[4];
                    System.arraycopy(messageBody, offset, arr, 0, arr.length);
                    message.addAdasAlarm("alarmId", ByteUtils.dword2long(arr));
                    offset += arr.length;

                    message.addAdasAlarm("flag", ByteUtils.byte2int(messageBody[offset++]));
                    int type = ByteUtils.byte2int(messageBody[offset++]);
                    message.addAdasAlarm("type", type);
                    message.addAdasAlarm("level", ByteUtils.byte2int(messageBody[offset++]));
                    if (type < 0x03) {
                        message.addAdasAlarm("frontSpeed", ByteUtils.byte2int(messageBody[offset++]));
                    } else {
                        offset++;
                    }

                    if (type < 0x03 || type == 0x04) {
                        message.addAdasAlarm("frontDistance", ByteUtils.byte2int(messageBody[offset++]));
                    } else {
                        offset++;
                    }

                    if (type == 0x02) {
                        message.addAdasAlarm("deviationType", ByteUtils.byte2int(messageBody[offset++]));
                    } else {
                        offset++;
                    }

                    if (type == 0x06 || type == 0x10) {
                        message.addAdasAlarm("roadSignType", ByteUtils.byte2int(messageBody[offset++]));
                        message.addAdasAlarm("roadSignData", ByteUtils.byte2int(messageBody[offset++]));
                    } else {
                        offset++;
                        offset++;
                    }

                    message.addAdasAlarm("speed", ByteUtils.byte2int(messageBody[offset++]));

                    arr = new byte[2];
                    System.arraycopy(messageBody, offset, arr, 0, arr.length);
                    altitude = ByteUtils.word2int(arr);
                    if (altitude > 32767) {
                        altitude -= 65536;
                    }
                    message.addAdasAlarm("altitude", altitude);
                    offset += arr.length;

                    arr = new byte[4];
                    System.arraycopy(messageBody, offset, arr, 0, arr.length);
                    message.addAdasAlarm("lat", String.valueOf(Double.valueOf(Double.valueOf(ByteUtils.dword2long(arr)).doubleValue() / 1000000.0D)));
                    offset += arr.length;

                    System.arraycopy(messageBody, offset, arr, 0, arr.length);
                    message.addAdasAlarm("lng", String.valueOf(Double.valueOf(Double.valueOf(ByteUtils.dword2long(arr)).doubleValue() / 1000000.0D)));
                    offset += arr.length;

                    arr = new byte[6];
                    System.arraycopy(messageBody, offset, arr, 0, arr.length);
                    message.addAdasAlarm("time", DateUtils.bcd2dateTime(ByteUtils.bytes2bcd(arr)));
                    offset += arr.length;

                    arr = new byte[2];
                    System.arraycopy(messageBody, offset, arr, 0, arr.length);
                    message.addAdasAlarm("state", ByteUtils.word2int(arr));
                    offset += arr.length;

                    arr = new byte[7];
                    System.arraycopy(messageBody, offset, arr, 0, arr.length);
                    message.addAdasAlarm("terminalId", ByteUtils.bytes2ascii(arr));
                    offset += arr.length;

                    arr = new byte[6];
                    System.arraycopy(messageBody, offset, arr, 0, arr.length);
                    message.addAdasAlarm("alarmTime", DateUtils.bcd2dateTime(ByteUtils.bytes2bcd(arr)));
                    offset += arr.length;

                    message.addAdasAlarm("alarmSeq", ByteUtils.byte2int(messageBody[offset++]));
                    message.addAdasAlarm("totalFile", ByteUtils.byte2int(messageBody[offset++]));
                    message.addAdasAlarm("alarmExt", ByteUtils.byte2int(messageBody[offset++]));
                } else if (param == 0x65) {// 驾驶员状态监测系统报警信息
                    arr = new byte[4];
                    System.arraycopy(messageBody, offset, arr, 0, arr.length);
                    message.addDsmAlarm("alarmId", ByteUtils.dword2long(arr));
                    offset += arr.length;

                    message.addDsmAlarm("flag", ByteUtils.byte2int(messageBody[offset++]));
                    int type = ByteUtils.byte2int(messageBody[offset++]);
                    message.addDsmAlarm("type", type);
                    message.addDsmAlarm("level", ByteUtils.byte2int(messageBody[offset++]));
                    if (type == 0x01) {
                        message.addDsmAlarm("fatigueDegree", ByteUtils.byte2int(messageBody[offset++]));
                    } else {
                        offset++;
                    }

                    System.arraycopy(messageBody, offset, arr, 0, arr.length);
                    message.addDsmAlarm("ext", ByteUtils.dword2long(arr));
                    offset += arr.length;

                    message.addDsmAlarm("speed", ByteUtils.byte2int(messageBody[offset++]));

                    arr = new byte[2];
                    System.arraycopy(messageBody, offset, arr, 0, arr.length);
                    altitude = ByteUtils.word2int(arr);
                    if (altitude > 32767) {
                        altitude -= 65536;
                    }
                    message.addDsmAlarm("altitude", altitude);
                    offset += arr.length;

                    arr = new byte[4];
                    System.arraycopy(messageBody, offset, arr, 0, arr.length);
                    message.addDsmAlarm("lat", String.valueOf(Double.valueOf(Double.valueOf(ByteUtils.dword2long(arr)).doubleValue() / 1000000.0D)));
                    offset += arr.length;

                    System.arraycopy(messageBody, offset, arr, 0, arr.length);
                    message.addDsmAlarm("lng", String.valueOf(Double.valueOf(Double.valueOf(ByteUtils.dword2long(arr)).doubleValue() / 1000000.0D)));
                    offset += arr.length;

                    arr = new byte[6];
                    System.arraycopy(messageBody, offset, arr, 0, arr.length);
                    message.addDsmAlarm("time", DateUtils.bcd2dateTime(ByteUtils.bytes2bcd(arr)));
                    offset += arr.length;

                    arr = new byte[2];
                    System.arraycopy(messageBody, offset, arr, 0, arr.length);
                    message.addDsmAlarm("state", ByteUtils.word2int(arr));
                    offset += arr.length;

                    arr = new byte[7];
                    System.arraycopy(messageBody, offset, arr, 0, arr.length);
                    message.addDsmAlarm("terminalId", ByteUtils.bytes2ascii(arr));
                    offset += arr.length;

                    arr = new byte[6];
                    System.arraycopy(messageBody, offset, arr, 0, arr.length);
                    message.addDsmAlarm("alarmTime", DateUtils.bcd2dateTime(ByteUtils.bytes2bcd(arr)));
                    offset += arr.length;

                    message.addDsmAlarm("alarmSeq", ByteUtils.byte2int(messageBody[offset++]));
                    message.addDsmAlarm("totalFile", ByteUtils.byte2int(messageBody[offset++]));
                    message.addDsmAlarm("alarmExt", ByteUtils.byte2int(messageBody[offset++]));
                } else if (param == 0x66) {// 胎压监测系统报警信息
                    arr = new byte[4];
                    System.arraycopy(messageBody, offset, arr, 0, arr.length);
                    message.addTpmAlarm("alarmId", ByteUtils.dword2long(arr));
                    offset += arr.length;

                    message.addTpmAlarm("flag", ByteUtils.byte2int(messageBody[offset++]));
                    message.addTpmAlarm("speed", ByteUtils.byte2int(messageBody[offset++]));

                    arr = new byte[2];
                    System.arraycopy(messageBody, offset, arr, 0, arr.length);
                    altitude = ByteUtils.word2int(arr);
                    if (altitude > 32767) {
                        altitude -= 65536;
                    }
                    message.addTpmAlarm("altitude", altitude);
                    offset += arr.length;

                    arr = new byte[4];
                    System.arraycopy(messageBody, offset, arr, 0, arr.length);
                    message.addTpmAlarm("lat", String.valueOf(Double.valueOf(Double.valueOf(ByteUtils.dword2long(arr)).doubleValue() / 1000000.0D)));
                    offset += arr.length;

                    System.arraycopy(messageBody, offset, arr, 0, arr.length);
                    message.addTpmAlarm("lng", String.valueOf(Double.valueOf(Double.valueOf(ByteUtils.dword2long(arr)).doubleValue() / 1000000.0D)));
                    offset += arr.length;

                    arr = new byte[6];
                    System.arraycopy(messageBody, offset, arr, 0, arr.length);
                    message.addTpmAlarm("time", DateUtils.bcd2dateTime(ByteUtils.bytes2bcd(arr)));
                    offset += arr.length;

                    arr = new byte[2];
                    System.arraycopy(messageBody, offset, arr, 0, arr.length);
                    message.addTpmAlarm("state", ByteUtils.word2int(arr));
                    offset += arr.length;

                    arr = new byte[7];
                    System.arraycopy(messageBody, offset, arr, 0, arr.length);
                    message.addTpmAlarm("terminalId", ByteUtils.bytes2ascii(arr));
                    offset += arr.length;

                    arr = new byte[6];
                    System.arraycopy(messageBody, offset, arr, 0, arr.length);
                    message.addTpmAlarm("alarmTime", DateUtils.bcd2dateTime(ByteUtils.bytes2bcd(arr)));
                    offset += arr.length;

                    message.addTpmAlarm("alarmSeq", ByteUtils.byte2int(messageBody[offset++]));
                    message.addTpmAlarm("totalFile", ByteUtils.byte2int(messageBody[offset++]));
                    message.addTpmAlarm("alarmExt", ByteUtils.byte2int(messageBody[offset++]));

                    int totalAlarm = ByteUtils.byte2int(messageBody[offset++]);

                    List<Map<String, Object>> alarmList = new ArrayList<Map<String, Object>>();
                    Map<String, Object> map;
                    for (int i = 0; i < totalAlarm; i++) {
                        map = new HashMap<String, Object>();
                        map.put("seq", ByteUtils.byte2int(messageBody[offset++]));

                        arr = new byte[2];
                        System.arraycopy(messageBody, offset, arr, 0, arr.length);
                        map.put("type", ByteUtils.word2int(arr));
                        offset += arr.length;

                        System.arraycopy(messageBody, offset, arr, 0, arr.length);
                        map.put("pressure", ByteUtils.word2int(arr));
                        offset += arr.length;

                        System.arraycopy(messageBody, offset, arr, 0, arr.length);
                        map.put("temperature", ByteUtils.word2int(arr));
                        offset += arr.length;

                        System.arraycopy(messageBody, offset, arr, 0, arr.length);
                        map.put("electricity", ByteUtils.word2int(arr));
                        offset += arr.length;

                        alarmList.add(map);
                    }
                    message.addTpmAlarm("alarmList", alarmList);
                } else if (param == 0x67) {// 盲区监测系统报警信息，
                    arr = new byte[4];
                    System.arraycopy(messageBody, offset, arr, 0, arr.length);
                    message.addBsdAlarm("alarmId", ByteUtils.dword2long(arr));
                    offset += arr.length;

                    message.addBsdAlarm("flag", ByteUtils.byte2int(messageBody[offset++]));
                    message.addDsmAlarm("type", ByteUtils.byte2int(messageBody[offset++]));
                    message.addBsdAlarm("speed", ByteUtils.byte2int(messageBody[offset++]));

                    arr = new byte[2];
                    System.arraycopy(messageBody, offset, arr, 0, arr.length);
                    altitude = ByteUtils.word2int(arr);
                    if (altitude > 32767) {
                        altitude -= 65536;
                    }
                    message.addBsdAlarm("altitude", altitude);
                    offset += arr.length;

                    arr = new byte[4];
                    System.arraycopy(messageBody, offset, arr, 0, arr.length);
                    message.addBsdAlarm("lat", String.valueOf(Double.valueOf(Double.valueOf(ByteUtils.dword2long(arr)).doubleValue() / 1000000.0D)));
                    offset += arr.length;

                    System.arraycopy(messageBody, offset, arr, 0, arr.length);
                    message.addBsdAlarm("lng", String.valueOf(Double.valueOf(Double.valueOf(ByteUtils.dword2long(arr)).doubleValue() / 1000000.0D)));
                    offset += arr.length;

                    arr = new byte[6];
                    System.arraycopy(messageBody, offset, arr, 0, arr.length);
                    message.addBsdAlarm("time", DateUtils.bcd2dateTime(ByteUtils.bytes2bcd(arr)));
                    offset += arr.length;

                    arr = new byte[2];
                    System.arraycopy(messageBody, offset, arr, 0, arr.length);
                    message.addBsdAlarm("state", ByteUtils.word2int(arr));
                    offset += arr.length;

                    arr = new byte[7];
                    System.arraycopy(messageBody, offset, arr, 0, arr.length);
                    message.addBsdAlarm("terminalId", ByteUtils.bytes2ascii(arr));
                    offset += arr.length;

                    arr = new byte[6];
                    System.arraycopy(messageBody, offset, arr, 0, arr.length);
                    message.addBsdAlarm("alarmTime", DateUtils.bcd2dateTime(ByteUtils.bytes2bcd(arr)));
                    offset += arr.length;

                    message.addBsdAlarm("alarmSeq", ByteUtils.byte2int(messageBody[offset++]));
                    message.addBsdAlarm("totalFile", ByteUtils.byte2int(messageBody[offset++]));
                    message.addBsdAlarm("alarmExt", ByteUtils.byte2int(messageBody[offset++]));
                }
                // *******tjsatl协议 end********//
                else if (param == 0x25) {
                    arr = new byte[4];
                    System.arraycopy(messageBody, offset, arr, 0, arr.length);
                    message.setExtSignal(ByteUtils.dword2long(arr));
                    offset += arr.length;
                } else if (param == 0x2A) {
                    arr = new byte[2];
                    System.arraycopy(messageBody, offset, arr, 0, arr.length);
                    message.setIo(ByteUtils.word2int(arr));
                    offset += arr.length;
                } else if (param == 0x2B) {
                    arr = new byte[4];
                    System.arraycopy(messageBody, offset, arr, 0, arr.length);
                    message.setAd(ByteUtils.dword2long(arr));
                    offset += arr.length;
                } else if (param == 0x30) {
                    arr = new byte[1];
                    System.arraycopy(messageBody, offset, arr, 0, arr.length);
                    message.setRssi(ByteUtils.byte2int(arr[0]));
                    offset += arr.length;
                } else if (param == 0x31) {
                    arr = new byte[1];
                    System.arraycopy(messageBody, offset, arr, 0, arr.length);
                    message.setGnss(ByteUtils.byte2int(arr[0]));
                    offset += arr.length;
                } else {
                    offset += length;
                }
            }
            return message;
        } catch (Exception e) {
            throw new IllegalMessageException(e);
        }
    }
}
