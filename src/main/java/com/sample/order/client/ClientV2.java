package com.sample.order.client;

import com.sample.order.client.codec.*;
import com.sample.order.client.codec.dispatcher.OperationResultFuture;
import com.sample.order.client.codec.dispatcher.ResponseDispatcherHandler;
import com.sample.order.client.codec.dispatcher.ResultPendingCenter;
import com.sample.order.common.OperationResult;
import com.sample.order.common.RequestMessage;
import com.sample.order.common.order.OrderOperation;
import com.sample.order.util.IdUtil;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

import java.util.concurrent.ExecutionException;

/**
 * 下单服务
 *
 * @author JueYi
 */
public class ClientV2 {


    public static void main(String[] args) throws InterruptedException, ExecutionException {

        Bootstrap bootstrap = new Bootstrap();
        bootstrap.channel(NioSocketChannel.class);

        NioEventLoopGroup group = new NioEventLoopGroup();

        ResultPendingCenter resultPendingCenter = new ResultPendingCenter();

        try {
            bootstrap.group(group);

            bootstrap.handler(new ChannelInitializer<SocketChannel>() {

                @Override
                protected void initChannel(SocketChannel ch) throws Exception {

                    ChannelPipeline pipeline = ch.pipeline();

                    pipeline.addLast(new OrderFrameDecoder());
                    pipeline.addLast(new OrderFrameEncoder());
                    pipeline.addLast(new OrderProtocolEncoder());
                    pipeline.addLast(new OrderProtocolDecoder());

                    pipeline.addLast(new ResponseDispatcherHandler(resultPendingCenter));

                    pipeline.addLast(new OperationToRequestMessageEncoder());

                    pipeline.addLast(new LoggingHandler(LogLevel.INFO));

                }
            });

            ChannelFuture channelFuture = bootstrap.connect("127.0.0.1", 8090).sync();
            // 先连上
            channelFuture.sync();

            OrderOperation orderOperation = new OrderOperation(1001, "tudou");


            long streamId = IdUtil.nextId();
            RequestMessage requestMessage = new RequestMessage(streamId, orderOperation);

            OperationResultFuture operationResultFuture = new OperationResultFuture();
            // 构建 streamId -> Future 关系
            resultPendingCenter.add(streamId, operationResultFuture);

            channelFuture.channel().writeAndFlush(requestMessage);
            System.out.println("--- writeAndFlush ---");
            // 阻塞拿到结果
            OperationResult operationResult = operationResultFuture.get();

            System.out.println(operationResult);

            channelFuture.channel().closeFuture().sync();

            System.out.println("Client !");

        } finally {
            group.shutdownGracefully();
        }
    }
}
