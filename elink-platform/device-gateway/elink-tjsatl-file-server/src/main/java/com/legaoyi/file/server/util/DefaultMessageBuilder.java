package com.legaoyi.file.server.util;

import com.legaoyi.file.messagebody.Tjsatl_2017_8001_MessageBody;
import com.legaoyi.protocol.message.Message;
import com.legaoyi.protocol.message.MessageHeader;

/**
 * @author <a href="mailto:shengbo.gao@gmail.com;78772895@qq.com">gaoshengbo</a>
 * @version 1.0.0
 * @since 2015-01-30
 */
public class DefaultMessageBuilder {

    public static Message build8001Message(int result) {
        String simCode = "000000000000";

        Message message = new Message();
        MessageHeader messageHeader = new MessageHeader();
        messageHeader.setMessageId(Tjsatl_2017_8001_MessageBody.MESSAGE_ID);
        messageHeader.setSimCode(simCode);

        Tjsatl_2017_8001_MessageBody messageBody = new Tjsatl_2017_8001_MessageBody();
        messageBody.setMessageId("0000");
        messageBody.setMessageSeq(0);
        messageBody.setResult(result);

        message.setMessageHeader(messageHeader);;
        message.setMessageBody(messageBody);
        return message;
    }

    public static Message build8001Message(Message message, int result) {
        if (message == null) {
            return null;
        }
        Tjsatl_2017_8001_MessageBody messageBody = new Tjsatl_2017_8001_MessageBody();
        messageBody.setMessageId(message.getMessageHeader().getMessageId());
        messageBody.setMessageSeq(message.getMessageHeader().getMessageSeq());
        messageBody.setResult(result);
        message.getMessageHeader().setMessageSeq(message.getMessageHeader().getMessageSeq());
        message.getMessageHeader().setMessageId(Tjsatl_2017_8001_MessageBody.MESSAGE_ID);
        message.setMessageBody(messageBody);
        return message;
    }
}
