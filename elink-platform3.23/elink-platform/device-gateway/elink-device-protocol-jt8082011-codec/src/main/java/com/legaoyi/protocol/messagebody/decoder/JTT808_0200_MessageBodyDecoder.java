package com.legaoyi.protocol.messagebody.decoder;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.legaoyi.protocol.exception.IllegalMessageException;
import com.legaoyi.protocol.message.MessageBody;
import com.legaoyi.protocol.message.decoder.MessageBodyDecoder;
import com.legaoyi.protocol.up.messagebody.JTT808_0200_MessageBody;
import com.legaoyi.protocol.util.ByteUtils;
import com.legaoyi.protocol.util.DateUtils;

/**
 * @author <a href="mailto:shengbo.gao@gmail.com;78772895@qq.com">gaoshengbo</a>
 * @version 1.0.0
 * @since 2015-01-30
 */
@Component(MessageBodyDecoder.MESSAGE_BODY_DECODER_BEAN_PREFIX + "0200_2011" + MessageBodyDecoder.MESSAGE_BODY_DECODER_BEAN_SUFFIX)
public class JTT808_0200_MessageBodyDecoder implements MessageBodyDecoder {

    @Override
    public MessageBody decode(byte[] messageBody) throws IllegalMessageException {
        JTT808_0200_MessageBody message = new JTT808_0200_MessageBody();
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
            message.setLat(String.format("%.6f", ByteUtils.dword2long(arr) * 0.000001));

            offset += arr.length;
            System.arraycopy(messageBody, offset, arr, 0, arr.length);
            message.setLng(String.format("%.6f", ByteUtils.dword2long(arr) * 0.000001));

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
                arr = new byte[1];
                System.arraycopy(messageBody, offset, arr, 0, arr.length);
                byte param = arr[0];
                offset += arr.length;

                System.arraycopy(messageBody, offset, arr, 0, arr.length);
                int length = ByteUtils.byte2int(arr[0]);
                offset += arr.length;

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
                    offset += arr.length;
                    
                    message.setInOutAlarmExt(inOutAlarmExt);
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
                } else if (param == 0x25) {
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
