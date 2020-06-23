package com.sample.order.server.handler;

import com.sample.order.common.Operation;
import com.sample.order.common.RequestMessage;
import com.sample.order.common.auth.AuthOperation;
import com.sample.order.common.auth.AuthOperationResult;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * 自定义授权
 *
 * @author JueYi
 */

@Slf4j
@ChannelHandler.Sharable
public class AuthHandler extends SimpleChannelInboundHandler<RequestMessage> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RequestMessage requestMessage) throws Exception {

        try {
            Operation operation = requestMessage.getMessageBody();

            if (operation instanceof AuthOperation) {
                AuthOperation authOperation = (AuthOperation) operation;
                AuthOperationResult authOperationResult = authOperation.execute();
                if (authOperationResult.isPassAuth()) {
                    log.info("pass auth");
                } else {
                    log.error("fail auth");
                }
            } else {
                log.error("expect first msg is auth ");
                ctx.close();
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            ctx.close();
        } finally {
            ctx.pipeline().remove(this);
        }

    }
}
