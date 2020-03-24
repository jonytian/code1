package com.legaoyi.protocol.down.messagebody;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.legaoyi.protocol.message.MessageBody;
/**
 * 设置车辆特征系数
 * 
 * @author <a href="mailto:shengbo.gao@gmail.com;78772895@qq.com">gaoshengbo</a>
 * @version 1.0.0
 * @since 2015-01-30
 */
@Scope("prototype")
@Component(MessageBody.MESSAGE_BODY_BEAN_PREFIX + "8701_C3H_2011" + MessageBody.MESSAGE_BODY_BEAN_SUFFIX)
public class JTT808_8701_C3H_MessageBody extends JTT808_8701_MessageBody {

    private static final long serialVersionUID = -6463742521217112673L;

    /** 车辆特征系数 **/
    @JsonProperty("coefficient")
    private int coefficient;

    public final int getCoefficient() {
        return coefficient;
    }

    public final void setCoefficient(int coefficient) {
        this.coefficient = coefficient;
    }

}
