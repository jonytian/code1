package com.legaoyi.protocol.downstream.messagebody;

import java.util.List;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonProperty;

import com.legaoyi.protocol.message.MessageBody;

/**
 * 查询区域或者路线数据
 * 
 * @author <a href="mailto:shengbo.gao@gmail.com;78772895@qq.com">gaoshengbo</a>
 * @version 1.0.0
 * @since 2019-05-20
 */
@Scope("prototype")
@Component(MessageBody.MESSAGE_BODY_BEAN_PREFIX + "8608_2019" + MessageBody.MESSAGE_BODY_BEAN_SUFFIX)
public class Jt808_2019_8608_MessageBody extends MessageBody {

    private static final long serialVersionUID = -3225041292826124334L;

    public static final String MESSAGE_ID = "8608";

    /** 类型 **/
    @JsonProperty("type")
    private int type;

    /** 区域id列表 **/
    @JsonProperty("areaIds")
    private List<Integer> areaIds;

    public final int getType() {
        return type;
    }

    public final void setType(int type) {
        this.type = type;
    }

    public final List<Integer> getAreaIds() {
        return areaIds;
    }

    public final void setAreaIds(List<Integer> areaIds) {
        this.areaIds = areaIds;
    }

}
