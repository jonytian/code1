package com.legaoyi.protocol.upstream.messagebody;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.legaoyi.protocol.message.MessageBody;

/**
 * 采集当前驾驶人信息
 * 
 * @author <a href="mailto:shengbo.gao@gmail.com;78772895@qq.com">gaoshengbo</a>
 * @version 1.0.0
 * @since 2019-05-20
 */
@Scope("prototype")
@Component(MessageBody.MESSAGE_BODY_BEAN_PREFIX + "0700_01H_2019" + MessageBody.MESSAGE_BODY_BEAN_SUFFIX)
public class Jt808_2019_0700_01H_MessageBody extends Jt808_2019_0700_MessageBody {

    private static final long serialVersionUID = -8046520912659747437L;

    /** 驾驶证号码 **/
    @JsonProperty("driverLicense")
    private String driverLicense;

    public final String getDriverLicense() {
        return driverLicense;
    }

    public final void setDriverLicense(String driverLicense) {
        this.driverLicense = driverLicense;
    }

}
