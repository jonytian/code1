package com.legaoyi.protocol.up.messagebody;

import java.util.List;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.legaoyi.protocol.message.MessageBody;
import com.legaoyi.protocol.up.messagebody.JTT808_0700_MessageBody;
/**
 * 采集指定的行驶速度记录(08H)
 * 
 * @author <a href="mailto:shengbo.gao@gmail.com;78772895@qq.com">gaoshengbo</a>
 * @version 1.0.0
 * @since 2015-01-30
 */
@Scope("prototype")
@Component(MessageBody.MESSAGE_BODY_BEAN_PREFIX + "0700_08H_2013" + MessageBody.MESSAGE_BODY_BEAN_SUFFIX)
public class JTT808_0700_08H_2013_MessageBody extends JTT808_0700_MessageBody {

    private static final long serialVersionUID = 3592290914945764548L;

    /** 数据块 **/
    @JsonProperty("dataList")
    private List<?> dataList;

    public final List<?> getDataList() {
        return dataList;
    }

    public final void setDataList(List<?> dataList) {
        this.dataList = dataList;
    }

}
