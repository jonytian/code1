package com.legaoyi.management.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

@Entity(name = "route")
@Table(name = "lbs_route")
@XmlRootElement
public class Route extends com.legaoyi.platform.model.BaseModel {

    private static final long serialVersionUID = -3563575406550821868L;

    public static final String ENTITY_NAME = "route";

    /** 路线名称 **/
    @Column(name = "name")
    private String name;

    /** 分组id **/
    @Column(name = "group_id")
    private String groupId;

    /** 路线属性 **/
    @Column(name = "attribute")
    private Integer attribute;

    /** 路段路宽 **/
    @Column(name = "width")
    private Integer width;

    /** 最高限速 **/
    @Column(name = "max_speed")
    private Integer maxSpeed;

    /** 路段超速持续时间 **/
    @Column(name = "duration_time")
    private Integer durationTime;

    /** 路段行驶过长阈值 **/
    @Column(name = "it_travel_time")
    private Integer itTravelTime;

    /** 路段行驶过长阈值 **/
    @Column(name = "gt_travel_time")
    private Integer gtTravelTime;

    /** 经纬度信息 **/
    @Column(name = "path")
    private String path;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public Integer getAttribute() {
        return attribute;
    }

    public void setAttribute(Integer attribute) {
        this.attribute = attribute;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public Integer getMaxSpeed() {
        return maxSpeed;
    }

    public void setMaxSpeed(Integer maxSpeed) {
        this.maxSpeed = maxSpeed;
    }

    public Integer getDurationTime() {
        return durationTime;
    }

    public void setDurationTime(Integer durationTime) {
        this.durationTime = durationTime;
    }

    public Integer getItTravelTime() {
        return itTravelTime;
    }

    public void setItTravelTime(Integer itTravelTime) {
        this.itTravelTime = itTravelTime;
    }

    public Integer getGtTravelTime() {
        return gtTravelTime;
    }

    public void setGtTravelTime(Integer gtTravelTime) {
        this.gtTravelTime = gtTravelTime;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

}
