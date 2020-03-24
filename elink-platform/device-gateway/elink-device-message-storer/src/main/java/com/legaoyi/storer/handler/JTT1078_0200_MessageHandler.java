package com.legaoyi.storer.handler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import com.legaoyi.common.message.ExchangeMessage;
import com.legaoyi.storer.handler.MessageHandler;
import com.legaoyi.storer.util.Constants;
import com.legaoyi.storer.util.IdGenerator;

/***
 * 1078协议
 * 
 * @author 高胜波
 *
 */
@Component(Constants.ELINK_MESSAGE_STORER_BEAN_PREFIX + "0200_2016" + Constants.ELINK_MESSAGE_STORER_MESSAGE_HANDLER_BEAN_SUFFIX)
public class JTT1078_0200_MessageHandler extends MessageHandler {

    @Autowired
    public JTT1078_0200_MessageHandler(@Qualifier(Constants.ELINK_MESSAGE_STORER_BEAN_PREFIX + "0200_2011" + Constants.ELINK_MESSAGE_STORER_MESSAGE_HANDLER_BEAN_SUFFIX) MessageHandler handler) {
        setSuccessor(handler);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void handle(ExchangeMessage message) throws Exception {
        Map<?, ?> map = (Map<?, ?>) message.getMessage();
        Map<String, Object> gps = (Map<String, Object>) map.get(Constants.MAP_KEY_MESSAGE_BODY);

        Map<String, Object> otherAlarm = new HashMap<String, Object>();
        /** 视频相关报警 **/

        Object o = gps.get("videoAlarm");
        Long videoAlarm = (o == null ? 0 : Long.parseLong(String.valueOf(o)));
        if (videoAlarm > 0) {
            otherAlarm.put("va", videoAlarm);
            otherAlarm.put("type", Constants.AlarmType.VIDEO.getType());
            if (((videoAlarm & (1 << 0)) >> 0) == 1) {
                /** 视频信号丢失报警状态 **/
                o = gps.get("cameraLoseAlarm");
                Long cameraLoseAlarm = (o == null ? 0 : Long.parseLong(String.valueOf(o)));
                otherAlarm.put("descVa0", cameraLoseAlarm);
            }

            if (((videoAlarm & (1 << 1)) >> 1) == 1) {
                /** 视频信号遮挡报警状态 **/
                o = gps.get("cameraCoverAlarm");
                Long cameraCoverAlarm = (o == null ? 0 : Long.parseLong(String.valueOf(o)));
                otherAlarm.put("descVa1", cameraCoverAlarm);
            }

            if (((videoAlarm & (1 << 2)) >> 2) == 1) {
                /** 存储器故障报警状态 **/
                o = gps.get("ramAlarm");
                Integer ramAlarm = (o == null ? 0 : Integer.parseInt(String.valueOf(o)));
                otherAlarm.put("descVa2", ramAlarm);
            }

            if (((videoAlarm & (1 << 5)) >> 5) == 1) {
                /** 异常驾驶行为报警状态 **/
                o = gps.get("drivingAlarm");
                Integer drivingAlarm = (o == null ? 0 : Integer.parseInt(String.valueOf(o)));
                otherAlarm.put("descVa5", drivingAlarm);
            }
        }

        if (!otherAlarm.isEmpty()) {
            // 苏标告警类型
            otherAlarm.put("id", IdGenerator.nextIdStr());
            otherAlarm.put("type", Constants.AlarmType.VIDEO.getType());

            List<Map<String, Object>> otherAlarmList = (List<Map<String, Object>>) message.getExtAttribute("alarmList");
            if (otherAlarmList == null) {
                otherAlarmList = new ArrayList<Map<String, Object>>();
            }
            otherAlarmList.add(otherAlarm);
            message.putExtAttribute("alarmList", otherAlarmList);
        }
        // 后续其他业务处理
        getSuccessor().handle(message);
    }
}
