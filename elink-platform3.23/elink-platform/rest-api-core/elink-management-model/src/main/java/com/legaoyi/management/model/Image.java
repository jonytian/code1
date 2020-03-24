package com.legaoyi.management.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

@Entity(name = "image")
@Table(name = "system_image")
@XmlRootElement
public class Image extends com.legaoyi.platform.model.BaseModel {

    private static final long serialVersionUID = 2731138400971419687L;

    public static final String ENTITY_NAME = "image";

    /** 图片标题 **/
    @Column(name = "title")
    private String title;

    /** 图片文件名 **/
    @Column(name = "file_name")
    private String fileName;

    /** 业务类型 **/
    @Column(name = "biz_type")
    private Short bizType;

    /** 关联的业务id **/
    @Column(name = "biz_id")
    private String bizId;

    /** 图片内容 **/
    @Lob
    @Column(name = "image")
    private byte[] image;

    /** 图片描述 **/
    @Column(name = "remark")
    private String remark;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Short getBizType() {
        return bizType;
    }

    public void setBizType(Short bizType) {
        this.bizType = bizType;
    }

    public String getBizId() {
        return bizId;
    }

    public void setBizId(String bizId) {
        this.bizId = bizId;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

}
