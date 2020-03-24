package com.legaoyi.management.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author <a href="mailto:shengbo.gao@gmail.com;78772895@qq.com">gaoshengbo</a>
 * @version 1.0.0
 * @since 2019-01-15
 */
@Entity(name = "systemTarget")
@Table(name = "system_notice_target")
@XmlRootElement
public class NoticeTarget extends com.legaoyi.platform.model.BaseModel {

    private static final long serialVersionUID = 5300716328017161678L;

    public static final String ENTITY_NAME = "systemTarget";

    @Column(name = "notice_id")
    private String noticeId;

    /** 公告目标类型：1：子公司；2：子公司部门 **/
    @Column(name = "target_type")
    private Short targetType;

    @Column(name = "targer_id")
    private String targerId;

    public String getNoticeId() {
        return noticeId;
    }

    public void setNoticeId(String noticeId) {
        this.noticeId = noticeId;
    }

    public Short getTargetType() {
        return targetType;
    }

    public void setTargetType(Short targetType) {
        this.targetType = targetType;
    }

    public String getTargerId() {
        return targerId;
    }

    public void setTargerId(String targerId) {
        this.targerId = targerId;
    }

}
