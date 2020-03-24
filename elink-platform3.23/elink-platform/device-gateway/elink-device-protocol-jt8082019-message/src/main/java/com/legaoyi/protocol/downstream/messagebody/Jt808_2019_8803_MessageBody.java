package com.legaoyi.protocol.downstream.messagebody;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.legaoyi.protocol.message.MessageBody;

/**
 * 存储多媒体数据上传
 * 
 * @author <a href="mailto:shengbo.gao@gmail.com;78772895@qq.com">gaoshengbo</a>
 * @version 1.0.0
 * @since 2019-05-20
 */
@Scope("prototype")
@Component(MessageBody.MESSAGE_BODY_BEAN_PREFIX + "8803_2019" + MessageBody.MESSAGE_BODY_BEAN_SUFFIX)
public class Jt808_2019_8803_MessageBody extends Jt808_2019_8802_MessageBody {

    private static final long serialVersionUID = 7083531540323102634L;

    public static final String MESSAGE_ID = "8803";

    /** 存储标识 **/
    @JsonProperty("saveFlag")
    private int saveFlag;

    public final int getSaveFlag() {
        return saveFlag;
    }

    public final void setSaveFlag(int saveFlag) {
        this.saveFlag = saveFlag;
    }

}
