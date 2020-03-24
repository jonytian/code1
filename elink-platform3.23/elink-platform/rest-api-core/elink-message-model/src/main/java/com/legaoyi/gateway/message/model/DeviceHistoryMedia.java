package com.legaoyi.gateway.message.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import com.legaoyi.management.model.BaseModel;

@Entity(name = "deviceHistoryMedia")
@Table(name = "device_history_media")
@XmlRootElement
public class DeviceHistoryMedia extends BaseModel {

	private static final long serialVersionUID = 3591783590375746029L;

	public static final String ENTITY_NAME = "deviceHistoryMedia";

	public static final int LOCATION_TYPE_SERVER = 1;

	public static final int LOCATION_TYPE_TERMINAL = 2;

	/** 消息流水号 **/
	@Column(name = "message_seq")
	private Integer messageSeq;

	/** 存储位置：1:服务器;2:终端 **/
	@Column(name = "location_type")
	private Integer locationType = 1;

	/** 业务类型：1：808协议上传；2：1078协议上传 **/
	@Column(name = "biz_type")
	private Integer bizType = 2;

	/** 业务id **/
	@Column(name = "biz_id")
	private String bizId;

	/**
	 * 0：平台下发指令；1：定时动作；2：抢劫报警触发；3：碰撞侧翻报警触发；其他保留
	 **/
	@Column(name = "event_code")
	private Integer eventCode = 0;

	/** 多媒体类型,0：音视频，1：音频，2：视频，3：视频音频 **/
	@Column(name = "resource_type")
	private Integer resourceType;

	/** 拍摄通道id **/
	@Column(name = "channel_id")
	private Integer channelId;

	/** 码流类型：0：主/子码流，1：主码流，2：子码流 **/
	@Column(name = "stream_type")
	private Integer streamType;

	/** 存储位置：0：主/灾备存储器，1：主存储器；2：灾备存储器 **/
	@Column(name = "store_type")
	private Integer storeType;

	/** 多媒体记录开始时间 **/
	@Column(name = "start_time")
	private String startTime;

	/** 多媒体记录结束时间 **/
	@Column(name = "end_time")
	private String endTime;

	/** 文件存放路径 **/
	@Column(name = "file_path")
	private String filePath;

	/** 文件大小 **/
	@Column(name = "file_size")
	private Long fileSize;

	/** 文件名 **/
	@Column(name = "file_name")
	private String fileName;

	/** 录制时间 **/
	@Column(name = "record_time")
	private String recordTime;

	/** 拍照时的位置信息 **/
	@Column(name = "gps_info")
	private String gpsInfo;

	/** 状态：0：暂停，1：上传中，2：取消，3：成功，4：失败 **/
	@Column(name = "state")
	private Integer state = 1;

	public Integer getMessageSeq() {
		return messageSeq;
	}

	public void setMessageSeq(Integer messageSeq) {
		this.messageSeq = messageSeq;
	}

	public Integer getBizType() {
		return bizType;
	}

	public void setBizType(Integer bizType) {
		this.bizType = bizType;
	}

	public String getBizId() {
		return bizId;
	}

	public void setBizId(String bizId) {
		this.bizId = bizId;
	}

	public Integer getLocationType() {
		return locationType;
	}

	public void setLocationType(Integer locationType) {
		this.locationType = locationType;
	}

	public Integer getEventCode() {
		return eventCode;
	}

	public void setEventCode(Integer eventCode) {
		this.eventCode = eventCode;
	}

	public Integer getResourceType() {
		return resourceType;
	}

	public void setResourceType(Integer resourceType) {
		this.resourceType = resourceType;
	}

	public Integer getChannelId() {
		return channelId;
	}

	public void setChannelId(Integer channelId) {
		this.channelId = channelId;
	}

	public Integer getStreamType() {
		return streamType;
	}

	public void setStreamType(Integer streamType) {
		this.streamType = streamType;
	}

	public Integer getStoreType() {
		return storeType;
	}

	public void setStoreType(Integer storeType) {
		this.storeType = storeType;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public Long getFileSize() {
		return fileSize;
	}

	public void setFileSize(Long fileSize) {
		this.fileSize = fileSize;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getRecordTime() {
		return recordTime;
	}

	public void setRecordTime(String recordTime) {
		this.recordTime = recordTime;
	}

	public String getGpsInfo() {
		return gpsInfo;
	}

	public void setGpsInfo(String gpsInfo) {
		this.gpsInfo = gpsInfo;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

}
