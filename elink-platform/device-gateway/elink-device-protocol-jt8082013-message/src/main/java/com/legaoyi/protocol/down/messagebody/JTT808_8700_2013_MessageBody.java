package com.legaoyi.protocol.down.messagebody;

import java.util.Map;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.legaoyi.common.util.JsonUtil;
import com.legaoyi.protocol.exception.IllegalMessageException;
import com.legaoyi.protocol.message.MessageBody;
import com.legaoyi.protocol.util.SpringBeanUtil;

/**
 * 行驶记录采集命令
 * 
 * @author <a href="mailto:shengbo.gao@gmail.com;78772895@qq.com">gaoshengbo</a>
 * @version 1.0.0
 * @since 2015-01-30
 */
@Scope("prototype")
@Component(MessageBody.MESSAGE_BODY_BEAN_PREFIX + "8700_2013" + MessageBody.MESSAGE_BODY_BEAN_SUFFIX)
public class JTT808_8700_2013_MessageBody extends MessageBody {

    private static final long serialVersionUID = -3928873466655778218L;

    public static final String MESSAGE_ID = "8700";

    public static final String HEAD_FLAG = "AA75";

    /** 命令字，见GB/T19056中的相关定义的命令字，如01H、02H **/
    @JsonProperty("commandWord")
    private String commandWord;

    public final String getCommandWord() {
        return commandWord;
    }

    public final void setCommandWord(String commandWord) {
        this.commandWord = commandWord;
    }

    @Override
    public MessageBody invoke(Object o) throws Exception {
        JTT808_8700_2013_MessageBody messageBody = null;
        if (o instanceof String) {
            messageBody = JsonUtil.convertStringToObject((String) o, JTT808_8700_2013_MessageBody.class);
        } else if (o instanceof Map) {
            messageBody = JsonUtil.convertStringToObject(JsonUtil.covertObjectToString(o), JTT808_8700_2013_MessageBody.class);
        }

        try {
            String messageId = MESSAGE_ID.concat("_").concat(messageBody.getCommandWord());
            messageBody = (JTT808_8700_2013_MessageBody) SpringBeanUtil.getMessageBody(messageId, "2013");
            if (o instanceof String) {
                messageBody = JsonUtil.convertStringToObject((String) o, messageBody.getClass());
            } else if (o instanceof Map) {
                messageBody = JsonUtil.convertStringToObject(JsonUtil.covertObjectToString(o), messageBody.getClass());
            }
        } catch (IllegalMessageException e) {
        }

        return messageBody;
    }

}
