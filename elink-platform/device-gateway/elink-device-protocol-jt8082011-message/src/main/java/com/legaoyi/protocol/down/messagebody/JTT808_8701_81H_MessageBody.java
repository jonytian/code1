package com.legaoyi.protocol.down.messagebody;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.legaoyi.protocol.message.MessageBody;
/**
 * 设置驾驶员代码，驾驶证号码
 * 
 * @author <a href="mailto:shengbo.gao@gmail.com;78772895@qq.com">gaoshengbo</a>
 * @version 1.0.0
 * @since 2015-01-30
 */
@Scope("prototype")
@Component(MessageBody.MESSAGE_BODY_BEAN_PREFIX + "8701_81H_2011" + MessageBody.MESSAGE_BODY_BEAN_SUFFIX)
public class JTT808_8701_81H_MessageBody extends JTT808_8701_MessageBody {

    private static final long serialVersionUID = -1965386260107206087L;

    /** 驾驶员代码 **/
    @JsonProperty("driverCode")
    private String driverCode;

    /** 驾驶证号码 **/
    @JsonProperty("driverLicense")
    private String driverLicense;

    public final String getDriverCode() {
        return driverCode;
    }

    public final void setDriverCode(String driverCode) {
        this.driverCode = driverCode;
    }

    public final String getDriverLicense() {
        return driverLicense;
    }

    public final void setDriverLicense(String driverLicense) {
        this.driverLicense = driverLicense;
    }

}
