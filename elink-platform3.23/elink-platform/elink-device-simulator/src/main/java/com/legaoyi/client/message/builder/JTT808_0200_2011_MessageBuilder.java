package com.legaoyi.client.message.builder;

import java.util.Map;

import org.springframework.stereotype.Component;
import com.legaoyi.client.util.Constants;
import com.legaoyi.client.up.messagebody.JTT808_0200_2011_MessageBody;
import com.legaoyi.protocol.message.MessageBody;

@Component(Constants.MESSAGE_BUILDER_BEAN_PREFIX + "0200_2011" + Constants.MESSAGE_BUILDER_BEAN_SUFFIX)
public class JTT808_0200_2011_MessageBuilder implements MessageBuilder {

    @Override
    public MessageBody build(Map<String, Object> map) throws Exception {
        Double lng = (Double) map.get("lng");
        Double lat = (Double) map.get("lat");

        Double speed = (Double) map.get("speed");
        if (speed == null) {
            speed = (double) getRandom(0, 100);
        }

        Integer direction = (Integer) map.get("direction");
        if (direction == null) {
            direction = getRandom(0, 360);
        }

        Integer altitude = (Integer) map.get("altitude");
        if (altitude == null) {
            altitude = getRandom(0, 500);
        }

        JTT808_0200_2011_MessageBody messageBody = new JTT808_0200_2011_MessageBody();
        messageBody.setLat(lat);
        messageBody.setLng(lng);
        messageBody.setAlarm(getRandom(3, 15000000));
        messageBody.setState(getRandom(3, 15000000));
        messageBody.setMileage(getRandom(1000, 1500));
        messageBody.setOilmass(getRandom(100, 300));
        messageBody.setSpeed(speed);
        messageBody.setDirection(direction);
        messageBody.setAltitude(altitude);
        messageBody.setGpsTime(System.currentTimeMillis());
        return messageBody;
    }

    private static int getRandom(int start, int end) {
        return (int) (start + Math.random() * end);
    }

}
