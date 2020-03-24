package com.legaoyi.storer.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.legaoyi.common.message.ExchangeMessage;
import com.legaoyi.storer.dao.GeneralDao;
import com.legaoyi.storer.service.GeneralService;
import com.legaoyi.storer.util.Constants;
import com.legaoyi.storer.util.IdGenerator;

@Transactional
@Service(Constants.ELINK_MESSAGE_STORER_BEAN_PREFIX + "0700" + Constants.ELINK_MESSAGE_STORER_MESSAGE_SERVICE_BEAN_SUFFIX)
public class JTT808_0700_MessageServiceImpl implements GeneralService {

    @Autowired
    @Qualifier(Constants.ELINK_MESSAGE_STORER_BEAN_PREFIX + "0700" + Constants.ELINK_MESSAGE_STORER_MESSAGE_DAO_BEAN_SUFFIX)
    private GeneralDao dvrInfoDao;

    @SuppressWarnings("unchecked")
    @Override
    public void batchSave(List<?> list) throws Exception {
        List<Map<String, Object>> dvrInfoList = new ArrayList<Map<String, Object>>();
        Map<String, Object> data;
        for (Object o : list) {
            ExchangeMessage message = (ExchangeMessage) o;
            Map<?, ?> m = (Map<?, ?>) message.getMessage();
            Map<String, Object> messageBody = (Map<String, Object>) m.get(Constants.MAP_KEY_MESSAGE_BODY);
            Map<String, Object> messageHeader = (Map<String, Object>) m.get(Constants.MAP_KEY_MESSAGE_HEADER);
            Map<?, ?> device = (Map<?, ?>) message.getExtAttribute(Constants.MAP_KEY_DEVICE);
            messageBody.remove(Constants.MAP_KEY_MESSAGE_SEQ);
            messageBody.remove("hexBodyData");
            data = new HashMap<String, Object>();
            data.put("commandWord", messageBody.get("commandWord"));
            messageBody.remove("commandWord");
            data.put("data", messageBody);
            data.putAll(messageHeader);
            data.remove(Constants.MAP_KEY_SIM_CODE);
            data.put(Constants.MAP_KEY_ENTERPRISE_ID, device.get(Constants.MAP_KEY_ENTERPRISE_ID));
            data.put(Constants.MAP_KEY_DEVICE_ID, device.get(Constants.MAP_KEY_DEVICE_ID));
            data.put("_id", IdGenerator.nextIdStr());
            data.put("createTime", message.getCreateTime());
            dvrInfoList.add(data);
        }
        dvrInfoDao.batchSave(dvrInfoList);
    }

}
