package com.legaoyi.platform.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.annotations.GenericGenerator;

@Entity(name = "district")
@Table(name = "lbs_district")
@XmlRootElement
public class District implements Serializable {

    private static final long serialVersionUID = 8593759801610933273L;

    public static final String ENTITY_NAME = "district";

    @GenericGenerator(name = "generator", strategy = "uuid")
    @GeneratedValue(generator = "generator")
    @Id
    @Column(name = "id", length = 32)
    private String id;

    @Column(name = "area_code")
    private String code;

    @Column(name = "area_name")
    private String name;

    @Column(name = "area_level")
    private Integer level;

    @Column(name = "parent_code")
    private String parentCode;

    @Column(name = "post_code")
    private String postCode;

    @Column(name = "city_code")
    private String cityCode;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public String getParentCode() {
        return parentCode;
    }

    public void setParentCode(String parentCode) {
        this.parentCode = parentCode;
    }

    public String getPostCode() {
        return postCode;
    }

    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

}
