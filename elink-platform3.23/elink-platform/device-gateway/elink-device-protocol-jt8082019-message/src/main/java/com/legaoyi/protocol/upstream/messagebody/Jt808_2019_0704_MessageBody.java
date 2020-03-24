package com.legaoyi.protocol.upstream.messagebody;

import java.util.List;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.legaoyi.protocol.message.MessageBody;

/**
 * 定位数据批量上传
 * 
 * @author <a href="mailto:shengbo.gao@gmail.com;78772895@qq.com">gaoshengbo</a>
 * @version 1.0.0
 * @since 2019-05-20
 */
@Scope("prototype")
@Component(MessageBody.MESSAGE_BODY_BEAN_PREFIX + "0704_2019" + MessageBody.MESSAGE_BODY_BEAN_SUFFIX)
public class Jt808_2019_0704_MessageBody extends MessageBody {

    private static final long serialVersionUID = 7700806649269928953L;

    public static final String MESSAGE_ID = "0704";

    /** 位置信息类型 **/
    @JsonProperty("type")
    private int type;

    /** GPS信息列表 **/
    @JsonProperty("gpsInfoList")
    private List<Jt808_2019_0200_MessageBody> gpsInfoList;

    public final int getType() {
        return type;
    }

    public final void setType(int type) {
        this.type = type;
    }

    public final List<Jt808_2019_0200_MessageBody> getGpsInfoList() {
        return gpsInfoList;
    }

    public final void setGpsInfoList(List<Jt808_2019_0200_MessageBody> gpsInfoList) {
        this.gpsInfoList = gpsInfoList;
    }

}
