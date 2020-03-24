package com.legaoyi.protocol.upstream.messagebody;

import java.util.List;
import java.util.Map;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.legaoyi.protocol.message.MessageBody;

/**
 * 采集记录仪状态信号配置信息(06H)
 * 
 * @author <a href="mailto:shengbo.gao@gmail.com;78772895@qq.com">gaoshengbo</a>
 * @version 1.0.0
 * @since 2019-05-20
 */
@Scope("prototype")
@Component(MessageBody.MESSAGE_BODY_BEAN_PREFIX + "0700_06H_2019" + MessageBody.MESSAGE_BODY_BEAN_SUFFIX)
public class Jt808_2019_0700_06H_MessageBody extends Jt808_2019_0700_MessageBody {

    private static final long serialVersionUID = 3967333775572778460L;

    /** 记录仪当前时间 **/
    @JsonProperty("realTime")
    private String realTime;

    /** 状态信号配置信息 **/
    @JsonProperty("signal")
    private List<Map<String, Object>> signal;

    public final String getRealTime() {
        return realTime;
    }

    public final void setRealTime(String realTime) {
        this.realTime = realTime;
    }

    public final List<Map<String, Object>> getSignal() {
        return signal;
    }

    public final void setSignal(List<Map<String, Object>> signal) {
        this.signal = signal;
    }

}
