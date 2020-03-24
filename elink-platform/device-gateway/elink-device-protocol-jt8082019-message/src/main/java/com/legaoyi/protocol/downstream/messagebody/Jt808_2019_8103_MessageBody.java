package com.legaoyi.protocol.downstream.messagebody;

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
 * @since 2019-05-20
 */
@Scope("prototype")
@Component(MessageBody.MESSAGE_BODY_BEAN_PREFIX + "8103_2019" + MessageBody.MESSAGE_BODY_BEAN_SUFFIX)
public class Jt808_2019_8103_MessageBody extends MessageBody {

    private static final long serialVersionUID = -6174276527381575817L;

    public static final String MESSAGE_ID = "8103";

    /** 参数项列表，key/val键值对，key对应参数id，参考协议表中的定义，如0001,val类型为DWORD、DWORD，BYTE类型的值需要转成十六进制 **/
    @JsonProperty("paramList")
    private Map<String, String> paramList;

    public final Map<String, String> getParamList() {
        return paramList;
    }

    public final void setParamList(Map<String, String> paramList) {
        this.paramList = paramList;
    }

}
