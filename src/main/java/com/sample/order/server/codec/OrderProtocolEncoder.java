package com.sample.order.server.codec;

import com.sample.order.common.ResponseMessage;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;

import java.util.List;

/**
 * OrderProtocol Encoder
 * 经过Handler处理后额的消息需要出站
 * 第一次编码 将自定义消息转为二进制ByteBuf
 * <p>
 * 注意点:
 * ByteBuf buffer = ctx.alloc().buffer();
 * ByteBuf buffer1 = ByteBufAllocator.DEFAULT.buffer();
 * <p>
 * 由于ByteBuf 实现是可以相互切换的 堆内内存 堆外内存 内存池 非内存池
 * ctx ByteBuf 实现是ServerBootStrap 指定的实现
 * DEFAULT 实现在未来版本不可控
 *
 * @author JueYi
 */
public class OrderProtocolEncoder extends MessageToMessageEncoder<ResponseMessage> {

    @Override
    protected void encode(ChannelHandlerContext ctx, ResponseMessage msg, List<Object> out) throws Exception {

        ByteBuf buffer = ctx.alloc().buffer();
        msg.encode(buffer);

        out.add(buffer);

    }
}
