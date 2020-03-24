package com.legaoyi.protocol.down.messagebody;

import java.util.List;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.legaoyi.protocol.message.MessageBody;

/**
 * 查询指定终端参数
 * 
 * @author <a href="mailto:shengbo.gao@gmail.com;78772895@qq.com">gaoshengbo</a>
 * @version 1.0.0
 * @since 2015-01-30
 */
@Scope("prototype")
@Component(MessageBody.MESSAGE_BODY_BEAN_PREFIX + "8106_2013" + MessageBody.MESSAGE_BODY_BEAN_SUFFIX)
public class JTT808_8106_2013_MessageBody extends MessageBody {

    private static final long serialVersionUID = 4547035064879672661L;

    public static final String MESSAGE_ID = "8106";

    /** 参数id列表，参数id值参考协议的定义，如0001 **/
    @JsonProperty("paramTypes")
    private List<String> paramTypes;

    public final List<String> getParamTypes() {
        return paramTypes;
    }

    public final void setParamTypes(List<String> paramTypes) {
        this.paramTypes = paramTypes;
    }

}
