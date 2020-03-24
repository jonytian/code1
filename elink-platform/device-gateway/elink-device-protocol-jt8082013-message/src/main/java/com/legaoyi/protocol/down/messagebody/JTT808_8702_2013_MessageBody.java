package com.legaoyi.protocol.down.messagebody;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.legaoyi.protocol.message.MessageBody;

/**
 * 上报驾驶员身份信息请求
 * 
 * @author <a href="mailto:shengbo.gao@gmail.com;78772895@qq.com">gaoshengbo</a>
 * @version 1.0.0
 * @since 2015-01-30
 */
@Scope("prototype")
@Component(MessageBody.MESSAGE_BODY_BEAN_PREFIX + "8702_2013" + MessageBody.MESSAGE_BODY_BEAN_SUFFIX)
public class JTT808_8702_2013_MessageBody extends MessageBody {

    private static final long serialVersionUID = -3928873466655778218L;

    public static final String MESSAGE_ID = "8702";

}
