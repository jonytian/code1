package com.legaoyi.protocol.upstream.messagebody;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.legaoyi.protocol.message.MessageBody;

/**
 * @author <a href="mailto:shengbo.gao@gmail.com;78772895@qq.com">gaoshengbo</a>
 * @version 1.0.0
 * @since 2019-05-20
 */
@Scope("prototype")
@Component(MessageBody.MESSAGE_BODY_BEAN_PREFIX + "0102_2019" + MessageBody.MESSAGE_BODY_BEAN_SUFFIX)
public class Jt808_2019_0102_MessageBody extends MessageBody {

    private static final long serialVersionUID = -6313237473030187434L;

    public static final String MESSAGE_ID = "0102";

    /** 鉴权码 **/
    @JsonProperty("authCode")
    private String authCode;

    /** 终端IMEI **/
    @JsonProperty("imei")
    private String imei;

    /** 终端软件版本 **/
    @JsonProperty("version")
    private String version;

    public final String getAuthCode() {
        return authCode;
    }

    public final void setAuthCode(String authCode) {
        this.authCode = authCode;
    }

    public final String getImei() {
        return imei;
    }

    public final void setImei(String imei) {
        this.imei = imei;
    }

    public final String getVersion() {
        return version;
    }

    public final void setVersion(String version) {
        this.version = version;
    }

}
