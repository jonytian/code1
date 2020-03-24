package com.legaoyi.protocol.server;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import io.netty.channel.ChannelHandler.Sharable;

import java.util.List;
import org.springframework.stereotype.Component;

/**
 * @author <a href="mailto:shengbo.gao@gmail.com;78772895@qq.com">gaoshengbo</a>
 * @version 1.0.0
 * @since 2015-01-30
 */
@Sharable
@Component("codecEncoder")
public final class CodecEncoder extends MessageToByteEncoder<List<byte[]>> {

    @Override
    protected void encode(ChannelHandlerContext ctx, List<byte[]> messageList, ByteBuf sendBuf) throws Exception {
        for (byte[] bytes : messageList) {
            ctx.channel().writeAndFlush(Unpooled.copiedBuffer(bytes));
        }
    }
}
