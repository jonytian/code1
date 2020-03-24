package com.legaoyi.protocol.up.messagebody;

import java.util.Map;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.legaoyi.protocol.message.MessageBody;

/**
 * 位置信息
 * 
 * @author <a href="mailto:shengbo.gao@gmail.com;78772895@qq.com">gaoshengbo</a>
 * @version 1.0.0
 * @since 2015-01-30
 */
@Scope("prototype")
@Component(MessageBody.MESSAGE_BODY_BEAN_PREFIX + "0200_2011" + MessageBody.MESSAGE_BODY_BEAN_SUFFIX)
public class JTT808_0200_MessageBody extends MessageBody {

    private static final long serialVersionUID = 5300716328017161678L;

    public static final String MESSAGE_ID = "0200";

    /** 报警位 **/
    @JsonProperty("alarm")
    private long alarm;

    /** 状态位 **/
    @JsonProperty("state")
    private long state;

    /** 经度 **/
    @JsonProperty("lng")
    private String lng;

    /** 纬度 **/
    @JsonProperty("lat")
    private String lat;

    /** 海拔高度 **/
    @JsonProperty("altitude")
    private Integer altitude;

    /** 速度 **/
    @JsonProperty("speed")
    private Double speed = 0d;

    /** 方向 **/
    @JsonProperty("direction")
    private Integer direction = 0;

    /** 位置记录时间 **/
    @JsonProperty("gpsTime")
    private Long gpsTime;

    /** 里程 **/
    @JsonProperty("mileage")
    private Double mileage = 0d;

    /** 油量 **/
    @JsonProperty("oilmass")
    private Double oilmass = 0d;

    /** 行驶记录仪速度 **/
    @JsonProperty("dvrSpeed")
    private Double dvrSpeed;

    /** 报警事件id **/
    @JsonProperty("alarmEventId")
    private Integer alarmEventId;

    /** 超速报警 **/
    @JsonProperty("overSpeedAlarmExt")
    private Map<String, Object> overSpeedAlarmExt;

    /** 进出区域报警 **/
    @JsonProperty("inOutAlarmExt")
    private Map<String, Object> inOutAlarmExt;

    /** 路段行驶时间不足或者超时报警 **/
    @JsonProperty("runningTimeAlarmExt")
    private Map<String, Object> runningTimeAlarmExt;

    /** 扩展车辆信号状态位 **/
    @JsonProperty("extSignal")
    private Long extSignal;

    /** io状态位 **/
    @JsonProperty("io")
    private Integer io;

    /** 模拟量 **/
    @JsonProperty("ad")
    private Long ad;

    /** 无线通信网络信号强度 **/
    @JsonProperty("rssi")
    private Integer rssi;

    /** 定位卫星数 **/
    @JsonProperty("gnss")
    private Integer gnss;

    public final long getAlarm() {
        return alarm;
    }

    public final void setAlarm(long alarm) {
        this.alarm = alarm;
    }

    public final long getState() {
        return state;
    }

    public final void setState(long state) {
        this.state = state;
    }

    public final String getLng() {
        return lng;
    }

    public final void setLng(String lng) {
        this.lng = lng;
    }

    public final String getLat() {
        return lat;
    }

    public final void setLat(String lat) {
        this.lat = lat;
    }

    public final Integer getAltitude() {
        return altitude;
    }

    public final void setAltitude(Integer altitude) {
        this.altitude = altitude;
    }

    public final Double getSpeed() {
        return speed;
    }

    public final void setSpeed(Double speed) {
        this.speed = speed;
    }

    public final Integer getDirection() {
        return direction;
    }

    public final void setDirection(Integer direction) {
        this.direction = direction;
    }

    public final Long getGpsTime() {
        return gpsTime;
    }

    public final void setGpsTime(Long gpsTime) {
        this.gpsTime = gpsTime;
    }

    public final Double getMileage() {
        return mileage;
    }

    public final void setMileage(Double mileage) {
        this.mileage = mileage;
    }

    public final Double getOilmass() {
        return oilmass;
    }

    public final void setOilmass(Double oilmass) {
        this.oilmass = oilmass;
    }

    public final Double getDvrSpeed() {
        return dvrSpeed;
    }

    public final void setDvrSpeed(Double dvrSpeed) {
        this.dvrSpeed = dvrSpeed;
    }

    public final Integer getAlarmEventId() {
        return alarmEventId;
    }

    public final void setAlarmEventId(Integer alarmEventId) {
        this.alarmEventId = alarmEventId;
    }

    public final Map<String, Object> getOverSpeedAlarmExt() {
        return overSpeedAlarmExt;
    }

    public final void setOverSpeedAlarmExt(Map<String, Object> overSpeedAlarmExt) {
        this.overSpeedAlarmExt = overSpeedAlarmExt;
    }

    public final Map<String, Object> getInOutAlarmExt() {
        return inOutAlarmExt;
    }

    public final void setInOutAlarmExt(Map<String, Object> inOutAlarmExt) {
        this.inOutAlarmExt = inOutAlarmExt;
    }

    public final Map<String, Object> getRunningTimeAlarmExt() {
        return runningTimeAlarmExt;
    }

    public final void setRunningTimeAlarmExt(Map<String, Object> runningTimeAlarmExt) {
        this.runningTimeAlarmExt = runningTimeAlarmExt;
    }

    public final Long getExtSignal() {
        return extSignal;
    }

    public final void setExtSignal(Long extSignal) {
        this.extSignal = extSignal;
    }

    public final Integer getIo() {
        return io;
    }

    public final void setIo(Integer io) {
        this.io = io;
    }

    public final Long getAd() {
        return ad;
    }

    public final void setAd(Long ad) {
        this.ad = ad;
    }

    public final Integer getRssi() {
        return rssi;
    }

    public final void setRssi(Integer rssi) {
        this.rssi = rssi;
    }

    public final Integer getGnss() {
        return gnss;
    }

    public final void setGnss(Integer gnss) {
        this.gnss = gnss;
    }

}
