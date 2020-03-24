package com.legaoyi.file.message.handler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.legaoyi.common.message.ExchangeMessage;
import com.legaoyi.file.messagebody.Tjsatl_2017_1210_MessageBody;
import com.legaoyi.file.server.ServerMessageHandler;
import com.legaoyi.file.server.util.Constants;
import com.legaoyi.file.server.util.DefaultMessageBuilder;
import com.legaoyi.protocol.message.Message;

import io.netty.channel.ChannelHandlerContext;

@Component("tjsatl_1210_MessageHandler")
public class Tjsatl_2017_1210_MessageHandler implements MessageHandler {

    @Autowired
    @Qualifier("serverMessageHandler")
    private ServerMessageHandler serverMessageHandler;

    @Autowired
    @Qualifier("deviceDownMessageDeliverer")
    private DeviceDownMessageDeliverer messageDeliverer;

    @Value("${message.auth.disable}")
    private boolean isAuthDisable;

    @Override
    public void handle(ChannelHandlerContext ctx, Message message) throws Exception {
        Tjsatl_2017_1210_MessageBody messageBody = (Tjsatl_2017_1210_MessageBody) message.getMessageBody();
        List<Map<String, Object>> fileList = messageBody.getFileList();
        Map<String, Object> fileMap = new HashMap<String, Object>();
        for (Map<String, Object> map : fileList) {
            fileMap.put((String) map.get("name"), map.get("size"));
        }
        ctx.channel().attr(Constants.ATTRIBUTE_SESSION_ATTACHMENT_LIST).set(fileMap);
        serverMessageHandler.handle(new ExchangeMessage(ExchangeMessage.MESSAGEID_GATEWAY_UP_MESSAGE, message.clone(), message.getMessageHeader().getMessageSeq() + ""));
        // 应答8001消息
        if (isAuthDisable) {
            ctx.channel().attr(Constants.ATTRIBUTE_SESSION_STATE).set("true");
            messageDeliverer.deliver(ctx, DefaultMessageBuilder.build8001Message(message, 0));
        } else {
            // 平台验证simCode，并应答8001通用应答
        }
    }

}
