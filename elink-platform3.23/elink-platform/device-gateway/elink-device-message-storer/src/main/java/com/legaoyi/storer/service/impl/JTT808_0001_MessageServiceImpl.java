package com.legaoyi.storer.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.legaoyi.storer.dao.DeviceUpMessageDao;
import com.legaoyi.common.message.ExchangeMessage;
import com.legaoyi.mq.MQMessageProducer;
import com.legaoyi.storer.dao.GeneralDao;
import com.legaoyi.storer.service.GeneralService;
import com.legaoyi.storer.util.Constants;
import com.legaoyi.storer.util.IdGenerator;
import com.legaoyi.storer.util.WebJmsMessage;

@Transactional
@Service(Constants.ELINK_MESSAGE_STORER_BEAN_PREFIX + "0001" + Constants.ELINK_MESSAGE_STORER_MESSAGE_SERVICE_BEAN_SUFFIX)
public class JTT808_0001_MessageServiceImpl implements GeneralService {

    private static final Logger logger = LoggerFactory.getLogger(JTT808_0001_MessageServiceImpl.class);

    @Autowired
    @Qualifier(Constants.ELINK_MESSAGE_STORER_BEAN_PREFIX + "0001" + Constants.ELINK_MESSAGE_STORER_MESSAGE_DAO_BEAN_SUFFIX)
    private GeneralDao generalDao;

    @Autowired
    @Qualifier("deviceUpMessageDao")
    private DeviceUpMessageDao deviceUpMessageDao;

    @Autowired
    @Qualifier("platformNotifyProducer")
    private MQMessageProducer platformNotifyProducer;

    @SuppressWarnings("unchecked")
    @Override
    public void batchSave(List<?> list) throws Exception {
        List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
        List<Map<String, Object>> respList = new ArrayList<Map<String, Object>>();

        Map<String, Object> data;
        Map<String, Object> map;
        for (Object o : list) {
            try {
                ExchangeMessage message = (ExchangeMessage) o;
                Map<?, ?> m = (Map<?, ?>) message.getMessage();
                Map<?, ?> device = (Map<?, ?>) message.getExtAttribute(Constants.MAP_KEY_DEVICE);
                Object deviceId = (String) device.get(Constants.MAP_KEY_DEVICE_ID);
                Object enterpriseId = device.get(Constants.MAP_KEY_ENTERPRISE_ID);
                Map<String, Object> messageHeader = (Map<String, Object>) m.get(Constants.MAP_KEY_MESSAGE_HEADER);
                Map<String, Object> messageBody = (Map<String, Object>) m.get(Constants.MAP_KEY_MESSAGE_BODY);

                data = new HashMap<String, Object>();
                data.put("_id", IdGenerator.nextIdStr());
                data.put(Constants.MAP_KEY_ENTERPRISE_ID, enterpriseId);
                data.put(Constants.MAP_KEY_DEVICE_ID, deviceId);
                data.put("createTime", message.getCreateTime());
                data.put("result", messageBody.get("result"));
                data.put("respMessageSeq", messageBody.get(Constants.MAP_KEY_MESSAGE_SEQ));
                data.put("respMessageId", messageBody.get(Constants.MAP_KEY_MESSAGE_ID));
                data.put(Constants.MAP_KEY_MESSAGE_SEQ, messageHeader.get(Constants.MAP_KEY_MESSAGE_SEQ));
                dataList.add(data);

                map = new HashMap<String, Object>();
                map.put(Constants.MAP_KEY_DEVICE_ID, deviceId);
                map.putAll(messageBody);
                respList.add(map);

                data.put(Constants.MAP_KEY_DEVICE_NAME, device.get(Constants.MAP_KEY_DEVICE_NAME));
                Object carId = device.get(Constants.MAP_KEY_CAR_ID);
                if (carId != null) {
                    data.put(Constants.MAP_KEY_CAR_ID, carId);
                    data.put(Constants.MAP_KEY_PLATE_NUMBER, device.get(Constants.MAP_KEY_PLATE_NUMBER));
                }
                //消息推送web页面
                platformNotifyProducer.send(new WebJmsMessage(WebJmsMessage.DOWNSTREAM_RESP_MESSAGE_TYPE, data).toString());
            } catch (Exception e) {
                logger.error("", e);
            }
        }
        generalDao.batchSave(dataList);
        deviceUpMessageDao.batchUpdate(respList);
    }
}
