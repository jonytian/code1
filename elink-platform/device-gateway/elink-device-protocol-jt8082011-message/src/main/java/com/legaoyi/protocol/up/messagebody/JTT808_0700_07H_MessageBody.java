package com.legaoyi.protocol.up.messagebody;

import java.util.List;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.legaoyi.protocol.message.MessageBody;
/**
 * 采集行驶记录仪事故疑点数据
 * 
 * @author <a href="mailto:shengbo.gao@gmail.com;78772895@qq.com">gaoshengbo</a>
 * @version 1.0.0
 * @since 2015-01-30
 */
@Scope("prototype")
@Component(MessageBody.MESSAGE_BODY_BEAN_PREFIX + "0700_07H_2011" + MessageBody.MESSAGE_BODY_BEAN_SUFFIX)
public class JTT808_0700_07H_MessageBody extends JTT808_0700_MessageBody {

    private static final long serialVersionUID = 1761370471709345924L;

    /** 数据疑点数据列表 **/
    @JsonProperty("accidentList")
    private List<?> accidentList;

    public final List<?> getAccidentList() {
        return accidentList;
    }

    public final void setAccidentList(List<?> accidentList) {
        this.accidentList = accidentList;
    }

}
