package com.legaoyi.protocol.up.messagebody;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.legaoyi.protocol.message.MessageBody;
import com.legaoyi.protocol.up.messagebody.JTT808_0700_MessageBody;
/**
 * 采集记录仪执行标准版本
 * 
 * @author <a href="mailto:shengbo.gao@gmail.com;78772895@qq.com">gaoshengbo</a>
 * @version 1.0.0
 * @since 2015-01-30
 */
@Scope("prototype")
@Component(MessageBody.MESSAGE_BODY_BEAN_PREFIX + "0700_00H_2013" + MessageBody.MESSAGE_BODY_BEAN_SUFFIX)
public class JTT808_0700_00H_2013_MessageBody extends JTT808_0700_MessageBody {

    private static final long serialVersionUID = -8046520912659747437L;

    /** 记录仪执行标准年号后 2 位 **/
    @JsonProperty("yearNo")
    private String yearNo;

    /** 修改单号 **/
    @JsonProperty("trackingNo")
    private String trackingNo;

    public final String getYearNo() {
        return yearNo;
    }

    public final void setYearNo(String yearNo) {
        this.yearNo = yearNo;
    }

    public final String getTrackingNo() {
        return trackingNo;
    }

    public final void setTrackingNo(String trackingNo) {
        this.trackingNo = trackingNo;
    }

}
