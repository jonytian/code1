package com.legaoyi.protocol.upstream.messagebody;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import com.legaoyi.protocol.message.MessageBody;

/**
 * @author <a href="mailto:shengbo.gao@gmail.com;78772895@qq.com">gaoshengbo</a>
 * @version 1.0.0
 * @since 2019-01-25
 */
@Scope("prototype")
@Component(MessageBody.MESSAGE_BODY_BEAN_PREFIX + "0110_2019" + MessageBody.MESSAGE_BODY_BEAN_SUFFIX)
public class Jt808_2019_0110_MessageBody extends MessageBody {

    private static final long serialVersionUID = -6313237473030187434L;

    public static final String MESSAGE_ID = "0110";

    private String ciphertext;

    public String getCiphertext() {
        return ciphertext;
    }

    public void setCiphertext(String ciphertext) {
        this.ciphertext = ciphertext;
    }
}
