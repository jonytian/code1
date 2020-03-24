package com.legaoyi.protocol.down.messagebody;

import java.util.List;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.legaoyi.protocol.message.MessageBody;

/**
 * 删除圆形区域
 * 
 * @author <a href="mailto:shengbo.gao@gmail.com;78772895@qq.com">gaoshengbo</a>
 * @version 1.0.0
 * @since 2015-01-30
 */
@Scope("prototype")
@Component(MessageBody.MESSAGE_BODY_BEAN_PREFIX + "8601_2011" + MessageBody.MESSAGE_BODY_BEAN_SUFFIX)
public class JTT808_8601_MessageBody extends MessageBody {

    private static final long serialVersionUID = 1086124614660196215L;

    public static final String MESSAGE_ID = "8601";

    /** 区域id列表 **/
    @JsonProperty("areaIds")
    private List<Integer> areaIds;

    public final List<Integer> getAreaIds() {
        return areaIds;
    }

    public final void setAreaIds(List<Integer> areaIds) {
        this.areaIds = areaIds;
    }

}
