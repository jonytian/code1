package com.legaoyi.protocol.upstream.messagebody;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.legaoyi.protocol.message.MessageBody;

/**
 * 电子运单上报
 * 
 * @author <a href="mailto:shengbo.gao@gmail.com;78772895@qq.com">gaoshengbo</a>
 * @version 1.0.0
 * @since 2019-05-20
 */
@Scope("prototype")
@Component(MessageBody.MESSAGE_BODY_BEAN_PREFIX + "0701_2019" + MessageBody.MESSAGE_BODY_BEAN_SUFFIX)
public class Jt808_2019_0701_MessageBody extends MessageBody {

    private static final long serialVersionUID = 2360483747739133170L;

    public static final String MESSAGE_ID = "0701";

    /** 电子运单内容 **/
    @JsonProperty("waybill")
    private String waybill;

    public final String getWaybill() {
        return waybill;
    }

    public final void setWaybill(String waybill) {
        this.waybill = waybill;
    }

}
