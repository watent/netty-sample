package com.sample.order.server.handler;

import com.sample.order.common.Operation;
import com.sample.order.common.OperationResult;
import com.sample.order.common.RequestMessage;
import com.sample.order.common.ResponseMessage;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * OrderServerProcess Handler
 * <p>
 * 继承 SimpleChannelInboundHandler 自动释放资源
 * <p>
 * note:
 * <p>
 * ctx.writeAndFlush() 从当前节点寻找下一个节点开始执行
 * ctx.channel().writeAndFlush() pipeline 重新执行一遍
 *
 * @author JueYi
 */

@Slf4j
public class OrderServerProcessHandler extends SimpleChannelInboundHandler<RequestMessage> {


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RequestMessage requestMessage) throws Exception {

        // 创建不释放 演示内存泄漏 防止日志被淹没 日志级别需要改为 ERROR
        // -Dio.netty.leakDetection.level=PARANOID
        ByteBuf bufferOOM = ctx.alloc().buffer();

        Operation operation = requestMessage.getMessageBody();
        OperationResult operationResult = operation.execute();

        ResponseMessage responseMessage = new ResponseMessage();
        responseMessage.setMessageHeader(requestMessage.getMessageHeader());
        responseMessage.setMessageBody(operationResult);

        if (ctx.channel().isActive() && ctx.channel().isWritable()) {
            ctx.writeAndFlush(responseMessage);
        } else {
            log.error("Message dropped !");
        }


    }
}
