package com.legaoyi.protocol.down.messagebody;

import java.util.List;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonProperty;

import com.legaoyi.protocol.message.MessageBody;

/**
 * 查询基本信息
 * 
 * @author <a href="mailto:shengbo.gao@gmail.com;78772895@qq.com">gaoshengbo</a>
 * @version 1.0.0
 * @since 2019-05-20
 */
@Scope("prototype")
@Component(MessageBody.MESSAGE_BODY_BEAN_PREFIX + "8900_tjsatl" + MessageBody.MESSAGE_BODY_BEAN_SUFFIX)
public class Tjsatl_2017_8900_MessageBody extends MessageBody {

    private static final long serialVersionUID = -6174276527381575817L;

    public static final String MESSAGE_ID = "8900";

    /** 透传消息类型 **/
    @JsonProperty("type")
    private int type;

    /** 外设ID列表 **/
    @JsonProperty("idList")
    private List<Integer> idList;

    public final int getType() {
        return type;
    }

    public final void setType(int type) {
        this.type = type;
    }

    public final List<Integer> getIdList() {
        return idList;
    }

    public final void setIdList(List<Integer> idList) {
        this.idList = idList;
    }

}
