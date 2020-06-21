package com.sample.order.client.codec.dispatcher;

import com.sample.order.common.ResponseMessage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * ResponseDispatcher Handler
 *
 * @author JueYi
 */
public class ResponseDispatcherHandler extends SimpleChannelInboundHandler<ResponseMessage> {

    private ResultPendingCenter resultPendingCenter;

    public ResponseDispatcherHandler(ResultPendingCenter resultPendingCenter) {
        this.resultPendingCenter = resultPendingCenter;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ResponseMessage msg) throws Exception {
        System.out.println("client Inbound ResponseDispatcherHandler.channelRead0()");
        resultPendingCenter.set(msg.getMessageHeader().getStreamId(), msg.getMessageBody());
    }
}
