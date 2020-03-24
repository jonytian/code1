package com.legaoyi.client;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import io.netty.channel.ChannelHandler.Sharable;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.legaoyi.protocol.util.ByteUtils;

/**
 * @author <a href="mailto:shengbo.gao@gmail.com;78772895@qq.com">gaoshengbo</a>
 * @version 1.0.0
 * @since 2015-01-30
 */
@Sharable
@Component("codecEncoder")
public final class CodecEncoder extends MessageToByteEncoder<List<byte[]>> {

    private static final Logger logger = LoggerFactory.getLogger(CodecEncoder.class);

    @Override
    protected void encode(ChannelHandlerContext ctx, List<byte[]> byteList, ByteBuf sendBuf) throws Exception {
        for (byte[] bytes : byteList) {
            logger.info("encode message, message={}", ByteUtils.bytes2hex(bytes));
            ctx.channel().writeAndFlush(Unpooled.copiedBuffer(bytes));
        }
    }
}
