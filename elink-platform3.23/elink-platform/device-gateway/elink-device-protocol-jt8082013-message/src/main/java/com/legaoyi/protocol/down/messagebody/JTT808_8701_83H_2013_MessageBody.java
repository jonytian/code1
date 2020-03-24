package com.legaoyi.protocol.down.messagebody;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.legaoyi.protocol.down.messagebody.JTT808_8701_2013_MessageBody;
import com.legaoyi.protocol.message.MessageBody;
/**
 * 设置记录仪初次安装日期
 * 
 * @author <a href="mailto:shengbo.gao@gmail.com;78772895@qq.com">gaoshengbo</a>
 * @version 1.0.0
 * @since 2015-01-30
 */
@Scope("prototype")
@Component(MessageBody.MESSAGE_BODY_BEAN_PREFIX + "8701_83H_2013" + MessageBody.MESSAGE_BODY_BEAN_SUFFIX)
public class JTT808_8701_83H_2013_MessageBody extends JTT808_8701_2013_MessageBody {

    private static final long serialVersionUID = 5635155346510917008L;

    /** 行驶记录实时仪时钟 **/
    @JsonProperty("realTime")
    private String realTime;

    public final String getRealTime() {
        return realTime;
    }

    public final void setRealTime(String realTime) {
        this.realTime = realTime;
    }

}
