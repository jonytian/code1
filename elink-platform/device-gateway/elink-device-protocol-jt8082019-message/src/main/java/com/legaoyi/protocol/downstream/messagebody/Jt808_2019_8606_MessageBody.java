package com.legaoyi.protocol.downstream.messagebody;

import java.util.List;
import java.util.Map;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonProperty;

import com.legaoyi.protocol.message.MessageBody;

/**
 * 设置路线
 * 
 * @author <a href="mailto:shengbo.gao@gmail.com;78772895@qq.com">gaoshengbo</a>
 * @version 1.0.0
 * @since 2019-05-20
 */
@Scope("prototype")
@Component(MessageBody.MESSAGE_BODY_BEAN_PREFIX + "8606_2019" + MessageBody.MESSAGE_BODY_BEAN_SUFFIX)
public class Jt808_2019_8606_MessageBody extends MessageBody {

    private static final long serialVersionUID = 8801280437560171104L;

    public static final String MESSAGE_ID = "8606";

    /** 路线id **/
    @JsonProperty("routeId")
    private int routeId;

    /** 路线属性，16位二进制，如：1100010000000000 **/
    @JsonProperty("attribute")
    private String attribute;

    /** 起始时间,格式如2014-12-22 23:00:02 **/
    @JsonProperty("startTime")
    private String startTime;

    /** 结束时间，如2014-12-28 23:00:02 **/
    @JsonProperty("endTime")
    private String endTime;

    /**
     * 路线拐点项列表，如：<br/>
     * List<Map<String,Object>> inflectionPointList = new ArrayList<Map<String,Object>>();//<br/>
     * Map<String,Object> map =new HashMap<String,Object>();//<br/>
     * map.put("pointId", 1);//拐点id<br/>
     * map.put("roadId", 1);//路段id，一条路线有n条路段，一个路段包含n个拐点<br/>
     * map.put("lng", 39.867200d);//拐点经度<br/>
     * map.put("lat", 116.350000d);//拐点纬度<br/>
     * map.put("roadWidth", 100);//路段路宽<br/>
     * map.put("roadAttribute", "10000000");//路段属性，8位二进制<br/>
     * map.put("ltTravelTime", 30);//路段行驶过长阈值<br/>
     * map.put("gtTravelTime", 10);/路段行驶不足阈值/<br/>
     * map.put("limitedSpeed",60);//最高限速<br/>
     * map.put("durationTime", 10);//路段超速持续时间<br/>
     * map.put("nightLimitedSpeed",60);//夜间最高限速<br/>
     * inflectionPointList.add(map);//<br/>
     * 
     **/
    @JsonProperty("inflectionPointList")
    private List<Map<String, Object>> inflectionPointList;

    @JsonProperty("name")
    private String name;

    public final int getRouteId() {
        return routeId;
    }

    public final void setRouteId(int routeId) {
        this.routeId = routeId;
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

    public final List<Map<String, Object>> getInflectionPointList() {
        return inflectionPointList;
    }

    public final void setInflectionPointList(List<Map<String, Object>> inflectionPointList) {
        this.inflectionPointList = inflectionPointList;
    }

    public final String getName() {
        return name;
    }

    public final void setName(String name) {
        this.name = name;
    }

}
