package com.legaoyi.client.message.builder;

import java.util.Map;

import org.springframework.stereotype.Component;
import com.legaoyi.client.util.Constants;
import com.legaoyi.client.up.messagebody.JTT808_0200_obd_MessageBody;
import com.legaoyi.protocol.message.MessageBody;

@Component(Constants.MESSAGE_BUILDER_BEAN_PREFIX + "0200_obd" + Constants.MESSAGE_BUILDER_BEAN_SUFFIX)
public class JTT808_0200_obd_MessageBuilder implements MessageBuilder {

    @Override
    public MessageBody build(Map<String, Object> map) throws Exception {
        Double lng = (Double) map.get("lng");
        Double lat = (Double) map.get("lat");

        JTT808_0200_obd_MessageBody messageBody = new JTT808_0200_obd_MessageBody();
        messageBody.setLat(lat);
        messageBody.setLng(lng);
        messageBody.setAlarm(getRandom(3, 15000000));
        messageBody.setState(getRandom(3, 15000000));
        messageBody.setMileage(getRandom(1000, 1500));
        messageBody.setOilmass(getRandom(50, 500));
        messageBody.setSpeed(getRandom(0, 100));
        messageBody.setDirection(getRandom(0, 360));
        messageBody.setAltitude(getRandom(0, 500));
        messageBody.setGpsTime(System.currentTimeMillis());

        messageBody.setE06(getRandom(30, 100));
        messageBody.setE14(getRandom(10, 100));
        messageBody.setE15(getRandom(0, 255));
        messageBody.setE16(getRandom(0, 99));
        messageBody.setE17(getRandom(40, 150));
        messageBody.setE18(getRandom(0, 10000000));
        messageBody.setE19(getRandom(0, 10000));
        messageBody.setE20(getRandom(0, 1000000));
        messageBody.setE21(getRandom(0, 180));
        messageBody.setE22(getRandom(0, 5000));
        messageBody.setE23(getRandom(0, 100));
        messageBody.setE24(getRandom(0, 100));
        return messageBody;
    }

    private static int getRandom(int start, int end) {
        return (int) (start + Math.random() * end);
    }

}
