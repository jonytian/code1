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
@Entity(name = "systemAlarm")
@Table(name = "system_alarm")
@XmlRootElement
public class SystemAlarm extends com.legaoyi.platform.model.BaseModel {

    private static final long serialVersionUID = 5300716328017161678L;

    public static final String ENTITY_NAME = "systemAlarm";

    /** 标题 **/
    @Column(name = "title")
    private String title;

    /** 内容 **/
    @Column(name = "content")
    private String content;

    /** 提醒类型：1：审批；2：车险到期；3：年检到期；4：驾驶证到期；5：接单提醒 **/
    @Column(name = "type")
    private Short type;

    /** 接收用户 **/
    @Column(name = "user_id")
    private String userId;

    /** 状态：0：未读；1：已阅 **/
    @Column(name = "state")
    private Short state;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Short getType() {
        return type;
    }

    public void setType(Short type) {
        this.type = type;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Short getState() {
        return state;
    }

    public void setState(Short state) {
        this.state = state;
    }

}
