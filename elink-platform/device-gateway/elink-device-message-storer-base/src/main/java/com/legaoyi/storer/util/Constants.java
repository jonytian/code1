package com.legaoyi.storer.util;

public class Constants {

	public static final String MAP_KEY_DEVICE = "device";

	public static final String MAP_KEY_DEVICE_ID = "deviceId";

	public static final String MAP_KEY_DEVICE_NAME = "deviceName";

	public static final String MAP_KEY_CAR_ID = "carId";

	public static final String MAP_KEY_PLATE_NUMBER = "plateNumber";

	public static final String MAP_KEY_PLATE_COLOR = "plateColor";

	public static final String MAP_KEY_MESSAGE_HEADER = "messageHeader";

	public static final String MAP_KEY_MESSAGE_BODY = "messageBody";

	public static final String MAP_KEY_MESSAGE_ID = "messageId";

	public static final String MAP_KEY_SIM_CODE = "simCode";

	public static final String MAP_KEY_MESSAGE_SEQ = "messageSeq";

	public static final String MAP_KEY_ENTERPRISE_ID = "enterpriseId";

	public static final short DEVICE_STATE_BATCH_SAVE_TYPE_ONLINE = 1;

	public static final short DEVICE_STATE_BATCH_SAVE_TYPE_OFFLINE = 2;

	public static final Short DEVICE_STATE_BATCH_SAVE_TYPE_REGISTERED = 3;

	public static final String ELINK_MESSAGE_STORER_BEAN_PREFIX = "elink_";

	public static final String ELINK_MESSAGE_STORER_MESSAGE_HANDLER_BEAN_SUFFIX = "_messageHandler";

	public static final String ELINK_MESSAGE_STORER_MESSAGE_SERVICE_BEAN_SUFFIX = "_messageService";

	public static final String ELINK_MESSAGE_STORER_MESSAGE_DAO_BEAN_SUFFIX = "_messageDao";

	public static final short BATCH_MESSAGE_TYPE_ON_OFF_LINE_LOG = 1;

	public static final short BATCH_MESSAGE_TYPE_DEVICE_STATE = 2;

	public static final short BATCH_MESSAGE_DEVICE_DOWN_MESSAGE = 3;

	public static final short BATCH_MESSAGE_DEVICE_UP_MESSAGE = 4;

	public static final short BATCH_MESSAGE_TYPE_ACC_STATE_LOG = 5;

	public static final short BATCH_MESSAGE_TYPE_DEVICE_BIZ_STATE = 6;

	public static final short BATCH_MESSAGE_TYPE_PARKING_LOG = 7;

	public static final short BATCH_MESSAGE_TYPE_DEVICE_FAULT = 8;

	public enum AlarmType {
		/** 终端设备故障告警 **/
		DEVICE_FAULT(1),
		/** 驾驶行为告警 **/
		DRIVING_BEHAVIOR(2),
		/** 紧急告警 **/
		DEVICE_URGENT(3),
		/** 平台报警 **/
		PLATFORM(4),
		/** 视频告警 **/
		VIDEO(5),
		/** 苏标告警 **/
		TJSATL(6);

		private int type;

		private AlarmType(int type) {
			this.type = type;
		}

		public final int getType() {
			return type;
		}

		public final void setType(int type) {
			this.type = type;
		}

	}

}
