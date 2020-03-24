package com.legaoyi.protocol.downstream.messagebody;

import java.util.Map;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonProperty;

import com.legaoyi.protocol.message.MessageBody;

/**
 * 车辆控制
 * 
 * @author <a href="mailto:shengbo.gao@gmail.com;78772895@qq.com">gaoshengbo</a>
 * @version 1.0.0
 * @since 2019-05-20
 */
@Scope("prototype")
@Component(MessageBody.MESSAGE_BODY_BEAN_PREFIX + "8500_2019" + MessageBody.MESSAGE_BODY_BEAN_SUFFIX)
public class Jt808_2019_8500_MessageBody extends MessageBody {

    private static final long serialVersionUID = -8541153336745142425L;

    public static final String MESSAGE_ID = "8500";

    /** 控制参数列表，key/val键值对，key、val为十六进制的值 **/
    @JsonProperty("command")
    private Map<String, String> command;

    public final Map<String, String> getCommand() {
        return command;
    }

    public final void setCommand(Map<String, String> command) {
        this.command = command;
    }

}
