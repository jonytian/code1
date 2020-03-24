package com.legaoyi.management.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

@Entity(name = "road")
@Table(name = "lbs_road")
@XmlRootElement
public class Road extends com.legaoyi.platform.model.BaseModel {

    private static final long serialVersionUID = -3563575406550821868L;

    public static final String ENTITY_NAME = "road";

    /** 路线名称 **/
    @Column(name = "name")
    private String name;

    /** 分组id **/
    @Column(name = "group_id")
    private String groupId;

    /** 路线属性 **/
    @Column(name = "attribute")
    private Integer attribute;

    /** 开始时间） **/
    @Column(name = "start_time")
    private String startTime;

    /** 结束时间 **/
    @Column(name = "end_time")
    private String endTime;

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

}
