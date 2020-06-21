package com.sample.order.server.codec;

import com.sample.order.common.RequestMessage;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;

import java.util.List;

/**
 * OrderProtocol Decoder
 * 第二次解码 将ByteBuf转换为自定义的Bean(这里为 RequestMessage)
 *
 * @author JueYi
 */
//@SuppressWarnings("unchecked")
public class OrderProtocolDecoder extends MessageToMessageDecoder<ByteBuf> {

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf byteBuf, List out) throws Exception {

        RequestMessage requestMessage = new RequestMessage();
        requestMessage.decode(byteBuf);
        out.add(requestMessage);
    }
}
