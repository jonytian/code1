package com.legaoyi.protocol.down.messagebody;

import java.util.List;
import java.util.Map;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.legaoyi.protocol.message.MessageBody;

/**
 * 事件设置
 * 
 * @author <a href="mailto:shengbo.gao@gmail.com;78772895@qq.com">gaoshengbo</a>
 * @version 1.0.0
 * @since 2015-01-30
 */
@Scope("prototype")
@Component(MessageBody.MESSAGE_BODY_BEAN_PREFIX + "8301_2011" + MessageBody.MESSAGE_BODY_BEAN_SUFFIX)
public class JTT808_8301_MessageBody extends MessageBody {

    private static final long serialVersionUID = 6849239253288036284L;

    public static final String MESSAGE_ID = "8301";

    /** 设置类型 **/
    @JsonProperty("type")
    private int type;

    /** 事项列表，key/val键值对，key包括事件id：eventId和事件内容：content **/
    @JsonProperty("eventList")
    private List<Map<String, Object>> eventList;

    public final int getType() {
        return type;
    }

    public final void setType(int type) {
        this.type = type;
    }

    public final List<Map<String, Object>> getEventList() {
        return eventList;
    }

    public final void setEventList(List<Map<String, Object>> eventList) {
        this.eventList = eventList;
    }

}
