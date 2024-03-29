package com.legaoyi.protocol.downstream.messagebody;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.legaoyi.protocol.message.MessageBody;

/**
 * 采集指定的事故疑点记录(10H)
 * 
 * @author <a href="mailto:shengbo.gao@gmail.com;78772895@qq.com">gaoshengbo</a>
 * @version 1.0.0
 * @since 2019-05-20
 */
@Scope("prototype")
@Component(MessageBody.MESSAGE_BODY_BEAN_PREFIX + "8700_10H_2019" + MessageBody.MESSAGE_BODY_BEAN_SUFFIX)
public class Jt808_2019_8700_10H_MessageBody extends Jt808_2019_8700_08H_MessageBody {

    private static final long serialVersionUID = -6463742521217112673L;

}
