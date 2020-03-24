package com.legaoyi.management.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

@Entity(name = "roadRoute")
@Table(name = "lbs_road_route")
@XmlRootElement
public class RoadRoute extends com.legaoyi.platform.model.BaseModel {

    private static final long serialVersionUID = -3563575406550821868L;

    public static final String ENTITY_NAME = "roadRoute";

    /** 路线id **/
    @Column(name = "road_id")
    private String roadId;

    /** 路线id **/
    @Column(name = "route_id")
    private String routeId;

    public String getRoadId() {
        return roadId;
    }

    public void setRoadId(String roadId) {
        this.roadId = roadId;
    }

    public String getRouteId() {
        return routeId;
    }

    public void setRouteId(String routeId) {
        this.routeId = routeId;
    }

}
