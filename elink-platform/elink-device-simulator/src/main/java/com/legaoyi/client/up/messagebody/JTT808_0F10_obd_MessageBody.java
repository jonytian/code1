package com.legaoyi.client.up.messagebody;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonProperty;

import com.legaoyi.protocol.message.MessageBody;

/**
 * @author <a href="mailto:shengbo.gao@gmail.com;78772895@qq.com">gaoshengbo</a>
 * @version 1.0.0
 * @since 2015-01-30
 */
@Scope("prototype")
@Component(MessageBody.MESSAGE_BODY_BEAN_PREFIX + "0F10_obd" + MessageBody.MESSAGE_BODY_BEAN_SUFFIX)
public class JTT808_0F10_obd_MessageBody extends MessageBody {

    private static final long serialVersionUID = -6313237473030187434L;

    public static final String JTT808_MESSAGE_ID = "0F10";

    /** 位置记录时间 **/
    @JsonProperty("gpsTime")
    private long gpsTime;

    /** 标志位 **/
    @JsonProperty("flag")
    private int flag;

    /** ASCI码格式传输，多个故障码用“|” **/
    @JsonProperty("code")
    private String code;

    public final long getGpsTime() {
        return gpsTime;
    }

    public final void setGpsTime(long gpsTime) {
        this.gpsTime = gpsTime;
    }

    public final int getFlag() {
        return flag;
    }

    public final void setFlag(int flag) {
        this.flag = flag;
    }

    public final String getCode() {
        return code;
    }

    public final void setCode(String code) {
        this.code = code;
    }

}
