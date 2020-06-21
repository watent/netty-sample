package com.sample.order.client.codec;

import com.sample.order.common.RequestMessage;
import com.sample.order.common.ResponseMessage;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;

import java.util.List;

/**
 * OrderProtocol Decoder
 *
 * @author JueYi
 */
//@SuppressWarnings("unchecked")
public class OrderProtocolDecoder extends MessageToMessageDecoder<ByteBuf> {

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf byteBuf, List out) throws Exception {
        System.out.println("client Inbound OrderProtocolDecoder");
        ResponseMessage responseMessage = new ResponseMessage();
        responseMessage.decode(byteBuf);
        out.add(responseMessage);
    }
}
