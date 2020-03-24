package com.legaoyi.protocol.up.messagebody;

import java.util.List;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.legaoyi.protocol.message.MessageBody;
import com.legaoyi.protocol.up.messagebody.JTT808_0200_MessageBody;

/**
 * 定位数据批量上传
 * 
 * @author <a href="mailto:shengbo.gao@gmail.com;78772895@qq.com">gaoshengbo</a>
 * @version 1.0.0
 * @since 2015-01-30
 */
@Scope("prototype")
@Component(MessageBody.MESSAGE_BODY_BEAN_PREFIX + "0704_2013" + MessageBody.MESSAGE_BODY_BEAN_SUFFIX)
public class JTT808_0704_2013_MessageBody extends MessageBody {

    private static final long serialVersionUID = 7700806649269928953L;

    public static final String MESSAGE_ID = "0704";

    /** 位置信息类型 **/
    @JsonProperty("type")
    private int type;

    /** GPS信息列表 **/
    @JsonProperty("gpsInfoList")
    private List<JTT808_0200_MessageBody> gpsInfoList;

    public final int getType() {
        return type;
    }

    public final void setType(int type) {
        this.type = type;
    }

    public final List<JTT808_0200_MessageBody> getGpsInfoList() {
        return gpsInfoList;
    }

    public final void setGpsInfoList(List<JTT808_0200_MessageBody> gpsInfoList) {
        this.gpsInfoList = gpsInfoList;
    }

}
