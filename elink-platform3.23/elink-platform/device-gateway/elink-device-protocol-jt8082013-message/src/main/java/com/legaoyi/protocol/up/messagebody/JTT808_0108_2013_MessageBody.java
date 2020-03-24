package com.legaoyi.protocol.up.messagebody;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.legaoyi.protocol.message.MessageBody;

/**
 * 终端升级结果通知
 * 
 * @author <a href="mailto:shengbo.gao@gmail.com;78772895@qq.com">gaoshengbo</a>
 * @version 1.0.0
 * @since 2015-01-30
 */
@Scope("prototype")
@Component(MessageBody.MESSAGE_BODY_BEAN_PREFIX + "0108_2013" + MessageBody.MESSAGE_BODY_BEAN_SUFFIX)
public class JTT808_0108_2013_MessageBody extends MessageBody {

    private static final long serialVersionUID = -3392914164582453750L;

    public static final String MESSAGE_ID = "0108";

    /** 升级类型 **/
    @JsonProperty("upgradeType")
    private int upgradeType;

    /** 升级结果 **/
    @JsonProperty("result")
    private int result;

    public final int getUpgradeType() {
        return upgradeType;
    }

    public final void setUpgradeType(int upgradeType) {
        this.upgradeType = upgradeType;
    }

    public final int getResult() {
        return result;
    }

    public final void setResult(int result) {
        this.result = result;
    }

}
