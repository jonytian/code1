package com.legaoyi.protocol.up.messagebody;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.legaoyi.protocol.message.MessageBody;
/**
 * 采集指定的超时驾驶记录(11H)
 * 
 * @author <a href="mailto:shengbo.gao@gmail.com;78772895@qq.com">gaoshengbo</a>
 * @version 1.0.0
 * @since 2015-01-30
 */
@Scope("prototype")
@Component(MessageBody.MESSAGE_BODY_BEAN_PREFIX + "0700_11H_2013" + MessageBody.MESSAGE_BODY_BEAN_SUFFIX)
public class JTT808_0700_11H_2013_MessageBody extends JTT808_0700_08H_2013_MessageBody {

    private static final long serialVersionUID = 6786948547364614837L;

}
