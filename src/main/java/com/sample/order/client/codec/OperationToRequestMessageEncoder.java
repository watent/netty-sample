package com.sample.order.client.codec;

import com.sample.order.common.Operation;
import com.sample.order.common.RequestMessage;
import com.sample.order.util.IdUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;

import java.util.List;

/**
 * OperationToRequestMessage Encoder
 *
 * @author JueYi
 */
public class OperationToRequestMessageEncoder extends MessageToMessageEncoder<Operation> {

    @Override
    protected void encode(ChannelHandlerContext ctx, Operation msg, List<Object> out) throws Exception {

        System.out.println("client Outbound OperationToRequestMessageEncoder");
        RequestMessage requestMessage = new RequestMessage(IdUtil.nextId(), msg);
        out.add(requestMessage);
    }
}
