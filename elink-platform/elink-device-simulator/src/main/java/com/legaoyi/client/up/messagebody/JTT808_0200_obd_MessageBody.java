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
@Component(MessageBody.MESSAGE_BODY_BEAN_PREFIX + "0200_obd" + MessageBody.MESSAGE_BODY_BEAN_SUFFIX)
public class JTT808_0200_obd_MessageBody extends MessageBody {

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

    /** 油箱剩余油量上传，显示范围（0~100%） **/
    private int e06;

    /** 发动机负荷上传，上传精度为千分之一，显示范围（0~100%） **/
    private int e14;

    /** 冷却液温度上传，(-40~215℃) ，偏移量40 **/
    private int e15;

    /** 电瓶电压上传，0-maxV，精度0.1V **/
    private int e16;

    /** 百公里油耗上传，精度1/10L **/
    private int e17;

    /** 累计驾驶时间数据上传，单位为0.01h **/
    private int e18;

    /** 累计怠速时间上传，单位为0.01h **/
    private int e19;

    /** 累计驾驶时间上传，单位为0.01h **/
    private int e20;

    /** 历史最高车速上传，km/h **/
    private int e21;

    /** 历史发动机最高转速上传，rpm **/
    private int e22;

    /** 累计急加速次数上传 **/
    private int e23;

    /** 累计急减速次数上传 **/
    private int e24;

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

    /**
     * @return the e06
     */
    public int getE06() {
        return e06;
    }

    /**
     * @param e06 the e06 to set
     */
    public void setE06(int e06) {
        this.e06 = e06;
    }

    /**
     * @return the e14
     */
    public int getE14() {
        return e14;
    }

    /**
     * @param e14 the e14 to set
     */
    public void setE14(int e14) {
        this.e14 = e14;
    }

    /**
     * @return the e15
     */
    public int getE15() {
        return e15;
    }

    /**
     * @param e15 the e15 to set
     */
    public void setE15(int e15) {
        this.e15 = e15;
    }

    /**
     * @return the e16
     */
    public int getE16() {
        return e16;
    }

    /**
     * @param e16 the e16 to set
     */
    public void setE16(int e16) {
        this.e16 = e16;
    }

    /**
     * @return the e17
     */
    public int getE17() {
        return e17;
    }

    /**
     * @param e17 the e17 to set
     */
    public void setE17(int e17) {
        this.e17 = e17;
    }

    /**
     * @return the e18
     */
    public int getE18() {
        return e18;
    }

    /**
     * @param e18 the e18 to set
     */
    public void setE18(int e18) {
        this.e18 = e18;
    }

    /**
     * @return the e19
     */
    public int getE19() {
        return e19;
    }

    /**
     * @param e19 the e19 to set
     */
    public void setE19(int e19) {
        this.e19 = e19;
    }

    /**
     * @return the e20
     */
    public int getE20() {
        return e20;
    }

    /**
     * @param e20 the e20 to set
     */
    public void setE20(int e20) {
        this.e20 = e20;
    }

    /**
     * @return the e21
     */
    public int getE21() {
        return e21;
    }

    /**
     * @param e21 the e21 to set
     */
    public void setE21(int e21) {
        this.e21 = e21;
    }

    /**
     * @return the e22
     */
    public int getE22() {
        return e22;
    }

    /**
     * @param e22 the e22 to set
     */
    public void setE22(int e22) {
        this.e22 = e22;
    }

    /**
     * @return the e23
     */
    public int getE23() {
        return e23;
    }

    /**
     * @param e23 the e23 to set
     */
    public void setE23(int e23) {
        this.e23 = e23;
    }

    /**
     * @return the e24
     */
    public int getE24() {
        return e24;
    }

    /**
     * @param e24 the e24 to set
     */
    public void setE24(int e24) {
        this.e24 = e24;
    }

}
