package com.legaoyi.protocol.down.messagebody;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.legaoyi.protocol.message.MessageBody;
/**
 * 删除多边形
 * 
 * @author <a href="mailto:shengbo.gao@gmail.com;78772895@qq.com">gaoshengbo</a>
 * @version 1.0.0
 * @since 2015-01-30
 */
@Scope("prototype")
@Component(MessageBody.MESSAGE_BODY_BEAN_PREFIX + "8605_2011" + MessageBody.MESSAGE_BODY_BEAN_SUFFIX)
public class JTT808_8605_MessageBody extends JTT808_8601_MessageBody {

    private static final long serialVersionUID = -8197623167751386191L;

    public static final String MESSAGE_ID = "8605";

}
