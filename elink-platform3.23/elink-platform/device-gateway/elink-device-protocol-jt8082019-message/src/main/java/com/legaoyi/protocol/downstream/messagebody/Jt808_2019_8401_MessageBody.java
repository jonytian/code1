package com.legaoyi.protocol.downstream.messagebody;

import java.util.List;
import java.util.Map;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.legaoyi.protocol.message.MessageBody;

/**
 * 设置电话本
 * 
 * @author <a href="mailto:shengbo.gao@gmail.com;78772895@qq.com">gaoshengbo</a>
 * @version 1.0.0
 * @since 2019-06-10
 */
@Scope("prototype")
@Component(MessageBody.MESSAGE_BODY_BEAN_PREFIX + "8401_2019" + MessageBody.MESSAGE_BODY_BEAN_SUFFIX)
public class Jt808_2019_8401_MessageBody extends MessageBody {

    private static final long serialVersionUID = -346205372929851176L;

    public static final String MESSAGE_ID = "8401";

    /** 设置类型 **/
    @JsonProperty("type")
    private int type;

    /** 联系人列表，key/val值，key包括标志:type,联系人：name,电话号码：tel **/
    @JsonProperty("itemList")
    private List<Map<String, Object>> itemList;

    public final int getType() {
        return type;
    }

    public final void setType(int type) {
        this.type = type;
    }

    public final List<Map<String, Object>> getItemList() {
        return itemList;
    }

    public final void setItemList(List<Map<String, Object>> itemList) {
        this.itemList = itemList;
    }

}
