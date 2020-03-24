package com.legaoyi.protocol.down.messagebody;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.legaoyi.protocol.message.MessageBody;
/**
 * 采集指定的记录仪外部供电记录(13H)
 * 
 * @author <a href="mailto:shengbo.gao@gmail.com;78772895@qq.com">gaoshengbo</a>
 * @version 1.0.0
 * @since 2015-01-30
 */
@Scope("prototype")
@Component(MessageBody.MESSAGE_BODY_BEAN_PREFIX + "8700_13H_2013" + MessageBody.MESSAGE_BODY_BEAN_SUFFIX)
public class JTT808_8700_13H_2013_MessageBody extends JTT808_8700_08H_2013_MessageBody {

    private static final long serialVersionUID = -6463742521217112673L;

}
