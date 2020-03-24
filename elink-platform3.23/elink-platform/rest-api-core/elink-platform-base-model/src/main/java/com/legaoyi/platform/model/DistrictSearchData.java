package com.legaoyi.platform.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

@Entity(name = "districtSearchData")
@Table(name = "lbs_district_search_data")
@XmlRootElement
public class DistrictSearchData implements Serializable {

    private static final long serialVersionUID = 8593759801610933273L;

    public static final String ENTITY_NAME = "districtSearchData";

    @Id
    @Column(name = "area_code")
    private String code;

    @Column(name = "area_name")
    private String name;

    @Column(name = "center_lng")
    private Double centerLng;

    @Column(name = "center_lat")
    private Double centerLat;

    @Column(name = "max_radius")
    private Integer maxRadius;

    @Column(name = "bounds")
    private String bounds;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getCenterLng() {
        return centerLng;
    }

    public void setCenterLng(Double centerLng) {
        this.centerLng = centerLng;
    }

    public Double getCenterLat() {
        return centerLat;
    }

    public void setCenterLat(Double centerLat) {
        this.centerLat = centerLat;
    }

    public Integer getMaxRadius() {
        return maxRadius;
    }

    public void setMaxRadius(Integer maxRadius) {
        this.maxRadius = maxRadius;
    }

    public String getBounds() {
        return bounds;
    }

    public void setBounds(String bounds) {
        this.bounds = bounds;
    }

}
