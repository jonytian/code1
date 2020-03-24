package com.legaoyi.protocol.upstream.messagebody;

import java.util.List;
import java.util.Map;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.legaoyi.protocol.message.MessageBody;

/**
 * CAN 总线数据上传
 * 
 * @author <a href="mailto:shengbo.gao@gmail.com;78772895@qq.com">gaoshengbo</a>
 * @version 1.0.0
 * @since 2019-05-20
 */
@Scope("prototype")
@Component(MessageBody.MESSAGE_BODY_BEAN_PREFIX + "0705_2019" + MessageBody.MESSAGE_BODY_BEAN_SUFFIX)
public class Jt808_2019_0705_MessageBody extends MessageBody {

    private static final long serialVersionUID = 7700806649269928953L;

    public static final String MESSAGE_ID = "0705";

    /** CAN 总线数据接收时间 **/
    @JsonProperty("dataTime")
    private String dataTime;

    /** CAN 总线数据项 **/
    @JsonProperty("dataList")
    private List<Map<String, Object>> dataList;

    public final String getDataTime() {
        return dataTime;
    }

    public final void setDataTime(String dataTime) {
        this.dataTime = dataTime;
    }

    public final List<Map<String, Object>> getDataList() {
        return dataList;
    }

    public final void setDataList(List<Map<String, Object>> dataList) {
        this.dataList = dataList;
    }

}
