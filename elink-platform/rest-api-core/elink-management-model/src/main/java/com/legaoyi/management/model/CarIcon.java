package com.legaoyi.management.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity(name = "carIcon")
@Table(name = "officers_car_icon")
@XmlRootElement
public class CarIcon extends com.legaoyi.platform.model.BaseModel {

    private static final long serialVersionUID = 2731138400971419687L;

    public static final String ENTITY_NAME = "carIcon";

    /** 图片标题 **/
    @Column(name = "title")
    private String title;

    /** 图片文件名 **/
    @Column(name = "file_name")
    private String fileName;

    /** 车辆颜色代码 **/
    @Column(name = "car_color")
    private Short carColor;

    /** 车辆品牌代码 **/
    @Column(name = "car_brand_type")
    private Short brandType;

    /** 车辆品牌代码 **/
    @Column(name = "car_brand_model")
    private Short brandModel;

    /** 图片内容 **/
    @JsonIgnore
    @Lob
    @Column(name = "icon")
    private byte[] icon;

    /** 图片描述 **/
    @Column(name = "remark")
    private String remark;

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFileName() {
        return this.fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Short getCarColor() {
        return this.carColor;
    }

    public void setCarColor(Short carColor) {
        this.carColor = carColor;
    }

    public Short getBrandType() {
        return this.brandType;
    }

    public void setBrandType(Short brandType) {
        this.brandType = brandType;
    }

    public Short getBrandModel() {
        return this.brandModel;
    }

    public void setBrandModel(Short brandModel) {
        this.brandModel = brandModel;
    }

    public byte[] getIcon() {
        return this.icon;
    }

    public void setIcon(byte[] icon) {
        this.icon = icon;
    }

    public String getRemark() {
        return this.remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

}
