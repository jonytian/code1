package com.legaoyi.protocol.up.messagebody;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.legaoyi.protocol.message.MessageBody;
/**
 * 采集行驶记录仪车辆特征系数应答
 * 
 * @author <a href="mailto:shengbo.gao@gmail.com;78772895@qq.com">gaoshengbo</a>
 * @version 1.0.0
 * @since 2015-01-30
 */
@Scope("prototype")
@Component(MessageBody.MESSAGE_BODY_BEAN_PREFIX + "0700_04H_2011" + MessageBody.MESSAGE_BODY_BEAN_SUFFIX)
public class JTT808_0700_04H_MessageBody extends JTT808_0700_MessageBody {

    private static final long serialVersionUID = 8562390939836556284L;

    /** 特征系数 **/
    @JsonProperty("coefficient")
    private int coefficient;

    public final int getCoefficient() {
        return coefficient;
    }

    public final void setCoefficient(int coefficient) {
        this.coefficient = coefficient;
    }

}
