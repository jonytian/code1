package com.legaoyi.protocol.down.messagebody;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.legaoyi.protocol.message.MessageBody;

/**
 * 车辆控制
 * 
 * @author <a href="mailto:shengbo.gao@gmail.com;78772895@qq.com">gaoshengbo</a>
 * @version 1.0.0
 * @since 2015-01-30
 */
@Scope("prototype")
@Component(MessageBody.MESSAGE_BODY_BEAN_PREFIX + "8500_2011" + MessageBody.MESSAGE_BODY_BEAN_SUFFIX)
public class JTT808_8500_MessageBody extends MessageBody {

    private static final long serialVersionUID = -8541153336745142425L;

    public static final String MESSAGE_ID = "8500";

    /** 控制标志，八位二进制字符串，如00000000 **/
    @JsonProperty("flag")
    private String flag;

    public final String getFlag() {
        return flag;
    }

    public final void setFlag(String flag) {
        this.flag = flag;
    }

}
