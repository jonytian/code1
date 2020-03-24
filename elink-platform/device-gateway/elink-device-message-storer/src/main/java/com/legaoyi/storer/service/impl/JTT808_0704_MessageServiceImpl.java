package com.legaoyi.storer.service.impl;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.legaoyi.common.message.ExchangeMessage;
import com.legaoyi.storer.dao.AlarmDao;
import com.legaoyi.storer.dao.GpsInfoDao;
import com.legaoyi.storer.service.DeviceService;
import com.legaoyi.storer.service.GeneralService;
import com.legaoyi.storer.util.Constants;
import com.legaoyi.storer.util.IdGenerator;

@Transactional
@Service(Constants.ELINK_MESSAGE_STORER_BEAN_PREFIX + "0704" + Constants.ELINK_MESSAGE_STORER_MESSAGE_SERVICE_BEAN_SUFFIX)
public class JTT808_0704_MessageServiceImpl implements GeneralService {

	@Autowired
	@Qualifier(Constants.ELINK_MESSAGE_STORER_BEAN_PREFIX + "0200" + Constants.ELINK_MESSAGE_STORER_MESSAGE_DAO_BEAN_SUFFIX)
	private GpsInfoDao gpsInfoDao;

	@Autowired
	@Qualifier("alarmDao")
	private AlarmDao gpsAlarmDao;

	@Autowired
	@Qualifier("deviceService")
	private DeviceService deviceService;

	@SuppressWarnings("unchecked")
	@Override
	public void batchSave(List<?> list) throws Exception {
		DateFormat df = new SimpleDateFormat("yyyyMMdd");
		List<Map<String, Object>> gpsInfoList = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> gpsAlarmList = new ArrayList<Map<String, Object>>();
		String lastDate = null;
		for (Object o : list) {
			ExchangeMessage message = (ExchangeMessage) o;
			Map<?, ?> m = (Map<?, ?>) message.getMessage();
			Map<String, Object> messageBody = (Map<String, Object>) m.get(Constants.MAP_KEY_MESSAGE_BODY);
			Map<?, ?> device = (Map<?, ?>) message.getExtAttribute(Constants.MAP_KEY_DEVICE);
			Object enterpriseId = device.get(Constants.MAP_KEY_ENTERPRISE_ID);
			Object deviceId = device.get(Constants.MAP_KEY_DEVICE_ID);
			Object carId = device.get(Constants.MAP_KEY_CAR_ID);

			int type = (Integer) messageBody.get("type");
			List<?> gpsInfoList1 = (List<?>) messageBody.get("gpsInfoList");
			for (Object o1 : gpsInfoList1) {
				Map<String, Object> gps = (Map<String, Object>) o1;

				gps.put(Constants.MAP_KEY_DEVICE_ID, deviceId);
				if (carId != null) {
					gps.put(Constants.MAP_KEY_CAR_ID, carId);
				}

				Long gpsTime = (Long) gps.get("gpsTime");
				String date = df.format(new Date(gpsTime));
				if (lastDate != null && !lastDate.equals(date)) {
					gpsInfoDao.batchSave(gpsInfoList, lastDate);
					gpsInfoList = new ArrayList<Map<String, Object>>();
				}

				if (gps.get("id") == null) {
					gps.put("id", IdGenerator.nextIdStr());
				}

				gps.put("type", type);// 补报位置信息
				gps.put(Constants.MAP_KEY_ENTERPRISE_ID, enterpriseId);
				gpsInfoList.add(gps);

				lastDate = date;
			}
		}

		if (!gpsInfoList.isEmpty()) {
			gpsInfoDao.batchSave(gpsInfoList, lastDate);
		}

		if (!gpsAlarmList.isEmpty()) {
			gpsAlarmDao.batchSave(gpsAlarmList);
		}
	}
}
