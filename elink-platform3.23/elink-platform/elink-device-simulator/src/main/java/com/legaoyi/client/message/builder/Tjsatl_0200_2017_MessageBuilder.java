package com.legaoyi.client.message.builder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;
import com.legaoyi.client.util.Constants;
import com.legaoyi.client.up.messagebody.Tjsatl_2017_0200_MessageBody;
import com.legaoyi.protocol.message.MessageBody;

@Component(Constants.MESSAGE_BUILDER_BEAN_PREFIX + "0200_tjsatl" + Constants.MESSAGE_BUILDER_BEAN_SUFFIX)
public class Tjsatl_0200_2017_MessageBuilder implements MessageBuilder {

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

        Tjsatl_2017_0200_MessageBody messageBody = new Tjsatl_2017_0200_MessageBody();
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

        int n = getRandom(0, 100) % 4;

        Map<String, Object> adasAlarm = new HashMap<String, Object>();
        adasAlarm.put("alarmId", getRandom(100, 800));
        adasAlarm.put("flag", 0);
        adasAlarm.put("type", getRandom(0, 11));
        adasAlarm.put("level", getRandom(1, 2));
        adasAlarm.put("frontSpeed", getRandom(20, 180));
        adasAlarm.put("frontDistance", getRandom(100, 1000));
        adasAlarm.put("deviationType", getRandom(1, 2));
        adasAlarm.put("roadSignType", getRandom(1, 3));
        adasAlarm.put("roadSignData", getRandom(0, 180));
        adasAlarm.put("speed", getRandom(10, 180));
        adasAlarm.put("altitude", getRandom(10, 5000));
        adasAlarm.put("terminalId", "a8903ds");
        adasAlarm.put("alarmSeq", getRandom(0, 220));
        adasAlarm.put("totalFile", getRandom(0, 5));
        adasAlarm.put("alarmExt", getRandom(10, 180));

        if (n == 0) {
            messageBody.setAdasAlarm(adasAlarm);
        } else if (n == 1) {
            Map<String, Object> dsmAlarm = new HashMap<String, Object>();
            dsmAlarm.putAll(adasAlarm);
            dsmAlarm.put("ext", getRandom(10, 180));
            dsmAlarm.put("fatigueDegree", getRandom(10, 100));
            messageBody.setDsmAlarm(dsmAlarm);
        } else if (n == 2) {
            Map<String, Object> tpmAlarm = new HashMap<String, Object>();
            tpmAlarm.putAll(adasAlarm);
            List<Map<String, Object>> alarmList = new ArrayList<Map<String, Object>>();
            Map<String, Object> alarm = new HashMap<String, Object>();
            alarm.put("seq", getRandom(0, 100));
            alarm.put("type", getRandom(0, 1000));
            alarm.put("pressure", getRandom(0, 5000));
            alarm.put("temperature", getRandom(0, 1000));
            alarm.put("electricity", getRandom(0, 100));
            alarmList.add(alarm);

            alarm = new HashMap<String, Object>();
            alarm.put("seq", getRandom(0, 100));
            alarm.put("type", getRandom(0, 1000));
            alarm.put("pressure", getRandom(0, 5000));
            alarm.put("temperature", getRandom(0, 1000));
            alarm.put("electricity", getRandom(0, 100));
            alarmList.add(alarm);

            alarm = new HashMap<String, Object>();
            alarm.put("seq", getRandom(0, 100));
            alarm.put("type", getRandom(0, 1000));
            alarm.put("pressure", getRandom(0, 5000));
            alarm.put("temperature", getRandom(0, 1000));
            alarm.put("electricity", getRandom(0, 100));
            alarmList.add(alarm);
            tpmAlarm.put("alarmList", alarmList);

            messageBody.setTpmAlarm(tpmAlarm);
        } else if (n == 3) {
            Map<String, Object> bsdAlarm = new HashMap<String, Object>();
            bsdAlarm.putAll(adasAlarm);
            messageBody.setBsdAlarm(bsdAlarm);
        }
        return messageBody;
    }

    private static int getRandom(int start, int end) {
        return (int) (start + Math.random() * end);
    }

}
