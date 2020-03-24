package com.legaoyi.protocol.up.messagebody;

import java.util.List;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.legaoyi.protocol.message.MessageBody;
/**
 * 采集最近2个日历天内同一驾驶员连续驾驶时间超过3个小时的所有记录数据
 * 
 * @author <a href="mailto:shengbo.gao@gmail.com;78772895@qq.com">gaoshengbo</a>
 * @version 1.0.0
 * @since 2015-01-30
 */
@Scope("prototype")
@Component(MessageBody.MESSAGE_BODY_BEAN_PREFIX + "0700_11H_2011" + MessageBody.MESSAGE_BODY_BEAN_SUFFIX)
public class JTT808_0700_11H_MessageBody extends JTT808_0700_MessageBody {

    private static final long serialVersionUID = 6786948547364614837L;

    /** 驾驶证号码 **/
    @JsonProperty("driverLicense")
    private String driverLicense;

    /** 数据列表 **/
    @JsonProperty("dataList")
    private List<?> dataList;

    public final String getDriverLicense() {
        return driverLicense;
    }

    public final void setDriverLicense(String driverLicense) {
        this.driverLicense = driverLicense;
    }

    public final List<?> getDataList() {
        return dataList;
    }

    public final void setDataList(List<?> dataList) {
        this.dataList = dataList;
    }

}
