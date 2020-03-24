package com.legaoyi.protocol.up.messagebody;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.legaoyi.protocol.message.MessageBody;

/**
 * 车辆控制应答
 * 
 * @author <a href="mailto:shengbo.gao@gmail.com;78772895@qq.com">gaoshengbo</a>
 * @version 1.0.0
 * @since 2019-05-20
 */
@Scope("prototype")
@Component(MessageBody.MESSAGE_BODY_BEAN_PREFIX + "0500_tjsatl" + MessageBody.MESSAGE_BODY_BEAN_SUFFIX)
public class Tjsatl_2017_0500_MessageBody extends Tjsatl_2017_0201_MessageBody {

    private static final long serialVersionUID = -346300311282868829L;

    public static final String MESSAGE_ID = "0500";

}
