package com.legaoyi.protocol.down.messagebody;

import java.util.Map;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.legaoyi.protocol.message.MessageBody;

/**
 * 设置终端参数
 * 
 * @author <a href="mailto:shengbo.gao@gmail.com;78772895@qq.com">gaoshengbo</a>
 * @version 1.0.0
 * @since 2018-04-10
 */
@Scope("prototype")
@Component(MessageBody.MESSAGE_BODY_BEAN_PREFIX + "8103_2016" + MessageBody.MESSAGE_BODY_BEAN_SUFFIX)
public class JTT1078_8103_MessageBody extends MessageBody {

    private static final long serialVersionUID = -6174276527381575817L;

    public static final String MESSAGE_ID = "8103";

    /** 参数项列表，key/val键值对，key对应参数id，参考协议表中的定义，val对应的参数值，按协议定义的顺序传递,如{0075:[]} **/
    @JsonProperty("paramList")
    private Map<String, Object> paramList;

    public final Map<String, Object> getParamList() {
        return paramList;
    }

    public final void setParamList(Map<String, Object> paramList) {
        this.paramList = paramList;
    }

}
