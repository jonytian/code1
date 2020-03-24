package com.legaoyi.client.up.messagebody;

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
public class JTT808_0200_2011_MessageBody extends MessageBody {

    private static final long serialVersionUID = 5300716328017161678L;

    public static final String JTT808_MESSAGE_ID = "0200";

    /** 报警位 **/
    @JsonProperty("alarm")
    private long alarm;

    /** 状态位 **/
    @JsonProperty("state")
    private long state;

    /** 经度 **/
    @JsonProperty("lng")
    private double lng;

    /** 纬度 **/
    @JsonProperty("lat")
    private double lat;

    /** 海拔高度 **/
    @JsonProperty("altitude")
    private int altitude = 800;

    /** 速度 **/
    @JsonProperty("speed")
    private double speed;

    /** 方向 **/
    @JsonProperty("direction")
    private int direction = 60;

    /** 位置记录时间 **/
    @JsonProperty("gpsTime")
    private long gpsTime;

    /** 里程 **/
    @JsonProperty("mileage")
    private double mileage = 0;

    /** 油量 **/
    @JsonProperty("oilmass")
    private double oilmass = 0;

    /** 行驶记录仪速度 **/
    @JsonProperty("dvrSpeed")
    private double dvrSpeed;

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

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public int getAltitude() {
        return altitude;
    }

    public void setAltitude(int altitude) {
        this.altitude = altitude;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

    public long getGpsTime() {
        return gpsTime;
    }

    public void setGpsTime(long gpsTime) {
        this.gpsTime = gpsTime;
    }

    public double getMileage() {
        return mileage;
    }

    public void setMileage(double mileage) {
        this.mileage = mileage;
    }

    public double getOilmass() {
        return oilmass;
    }

    public void setOilmass(double oilmass) {
        this.oilmass = oilmass;
    }

    public double getDvrSpeed() {
        return dvrSpeed;
    }

    public void setDvrSpeed(double dvrSpeed) {
        this.dvrSpeed = dvrSpeed;
    }

}
