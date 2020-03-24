package com.legaoyi.protocol.downstream.messagebody;

import java.util.List;
import java.util.Map;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonProperty;

import com.legaoyi.protocol.message.MessageBody;

/**
 * 设置多边形区域
 * 
 * @author <a href="mailto:shengbo.gao@gmail.com;78772895@qq.com">gaoshengbo</a>
 * @version 1.0.0
 * @since 2019-05-20
 */
@Scope("prototype")
@Component(MessageBody.MESSAGE_BODY_BEAN_PREFIX + "8604_2019" + MessageBody.MESSAGE_BODY_BEAN_SUFFIX)
public class Jt808_2019_8604_MessageBody extends MessageBody {

    private static final long serialVersionUID = 248276715427525407L;

    public static final String MESSAGE_ID = "8604";

    /** 区域id **/
    @JsonProperty("areaId")
    private int areaId;

    /** 区域属性，二进制字符串，如0111110000000000 **/
    @JsonProperty("attribute")
    private String attribute;

    /** 起始时间 **/
    @JsonProperty("startTime")
    private String startTime;

    /** 结束时间 **/
    @JsonProperty("endTime")
    private String endTime;

    /** 最高速度 **/
    @JsonProperty("limitedSpeed")
    private int limitedSpeed;

    /** 超速持续时间 **/
    @JsonProperty("durationTime")
    private int durationTime;

    /**
     * 顶点项列表，如 List<Map<String,Double>> vertexList = new ArrayList<Map<String,Double>>();<br/>
     * Map<String,Double> map =new HashMap<String,Double>();<br/>
     * map.put("lng", 39.58d);<br/>
     * map.put("lat", 116.05678d);<br/>
     * vertexList.add(map);<br/>
     **/
    @JsonProperty("vertexList")
    private List<Map<String, Double>> vertexList;

    /** 夜间最高速度 **/
    @JsonProperty("nightLimitedSpeed")
    private int nightLimitedSpeed;

    /** 区域名称 **/
    private String name;

    public final int getAreaId() {
        return areaId;
    }

    public final void setAreaId(int areaId) {
        this.areaId = areaId;
    }

    public final String getAttribute() {
        return attribute;
    }

    public final void setAttribute(String attribute) {
        this.attribute = attribute;
    }

    public final String getStartTime() {
        return startTime;
    }

    public final void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public final String getEndTime() {
        return endTime;
    }

    public final void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public final int getLimitedSpeed() {
        return limitedSpeed;
    }

    public final void setLimitedSpeed(int limitedSpeed) {
        this.limitedSpeed = limitedSpeed;
    }

    public final int getDurationTime() {
        return durationTime;
    }

    public final void setDurationTime(int durationTime) {
        this.durationTime = durationTime;
    }

    public final List<Map<String, Double>> getVertexList() {
        return vertexList;
    }

    public final void setVertexList(List<Map<String, Double>> vertexList) {
        this.vertexList = vertexList;
    }

    public final int getNightLimitedSpeed() {
        return nightLimitedSpeed;
    }

    public final void setNightLimitedSpeed(int nightLimitedSpeed) {
        this.nightLimitedSpeed = nightLimitedSpeed;
    }

    public final String getName() {
        return name;
    }

    public final void setName(String name) {
        this.name = name;
    }

}
