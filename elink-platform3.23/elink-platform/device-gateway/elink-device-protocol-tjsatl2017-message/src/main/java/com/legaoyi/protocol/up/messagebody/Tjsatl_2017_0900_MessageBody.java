package com.legaoyi.protocol.up.messagebody;

import java.util.List;
import java.util.Map;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.legaoyi.protocol.message.MessageBody;

/**
 * 上传基本信息
 * 
 * @author <a href="mailto:shengbo.gao@gmail.com;78772895@qq.com">gaoshengbo</a>
 * @version 1.0.0
 * @since 2019-05-20
 */
@Scope("prototype")
@Component(MessageBody.MESSAGE_BODY_BEAN_PREFIX + "0900_tjsatl" + MessageBody.MESSAGE_BODY_BEAN_SUFFIX)
public class Tjsatl_2017_0900_MessageBody extends MessageBody {

    private static final long serialVersionUID = 7643584698183762496L;

    /** 透传消息类型,状态查询: 0xF7,外设状态信息：外设工作状态、设备报警信息;信息查询:0xF8,外设传感器的基本信息：公司信息、 产品代码、 版本号、 外设 ID、 客户代码 **/
    @JsonProperty("type")
    private int type;

    @JsonProperty("messageList")
    private List<Map<String, Object>> messageList;

    public final int getType() {
        return type;
    }

    public final void setType(int type) {
        this.type = type;
    }

    public final List<Map<String, Object>> getMessageList() {
        return messageList;
    }

    public final void setMessageList(List<Map<String, Object>> messageList) {
        this.messageList = messageList;
    }

}
