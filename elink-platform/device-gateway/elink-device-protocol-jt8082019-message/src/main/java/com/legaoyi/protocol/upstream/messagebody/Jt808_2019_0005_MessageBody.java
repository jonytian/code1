package com.legaoyi.protocol.upstream.messagebody;

import java.util.List;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.legaoyi.protocol.message.MessageBody;

/**
 * 终端补传分包请求
 * 
 * @author <a href="mailto:shengbo.gao@gmail.com;78772895@qq.com">gaoshengbo</a>
 * @version 1.0.0
 * @since 2019-05-20
 */
@Scope("prototype")
@Component(MessageBody.MESSAGE_BODY_BEAN_PREFIX + "0005_2019" + MessageBody.MESSAGE_BODY_BEAN_SUFFIX)
public class Jt808_2019_0005_MessageBody extends MessageBody {

    private static final long serialVersionUID = -4151451507772016428L;

    public static final String MESSAGE_ID = "0005";

    /** 原始消息流水号 **/
    @JsonProperty("messageSeq")
    private int messageSeq;

    /** 重传包id列表 **/
    @JsonProperty("packageIds")
    private List<Integer> packageIds;

    public final int getMessageSeq() {
        return messageSeq;
    }

    public final void setMessageSeq(int messageSeq) {
        this.messageSeq = messageSeq;
    }

    public final List<Integer> getPackageIds() {
        return packageIds;
    }

    public final void setPackageIds(List<Integer> packageIds) {
        this.packageIds = packageIds;
    }
}
