package com.legaoyi.protocol.up.messagebody;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import com.legaoyi.protocol.message.MessageBody;

/**
 * 文件上传完成消息,终端向附件服务器发送报警附件信息指令并得到应答后，向附件服务器发送附件文件信息消息，
 * 
 * @author <a href="mailto:shengbo.gao@gmail.com;78772895@qq.com">gaoshengbo</a>
 * @version 1.0.0
 * @since 2019-05-20
 */
@Scope("prototype")
@Component(MessageBody.MESSAGE_BODY_BEAN_PREFIX + "1212_tjsatl" + MessageBody.MESSAGE_BODY_BEAN_SUFFIX)
public class Tjsatl_2017_1212_MessageBody extends Tjsatl_2017_1211_MessageBody {

    private static final long serialVersionUID = 7643584698183762496L;

}
