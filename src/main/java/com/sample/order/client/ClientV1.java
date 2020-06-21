package com.sample.order.client;

import com.sample.order.client.codec.*;
import com.sample.order.common.order.OrderOperation;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

/**
 * 下单服务
 *
 * @author JueYi
 */
public class ClientV1 {


    public static void main(String[] args) throws InterruptedException {

        Bootstrap bootstrap = new Bootstrap();
        bootstrap.channel(NioSocketChannel.class);

        NioEventLoopGroup group = new NioEventLoopGroup();

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

                    pipeline.addLast(new OperationToRequestMessageEncoder());

                    pipeline.addLast(new LoggingHandler(LogLevel.INFO));

                }
            });

            ChannelFuture channelFuture = bootstrap.connect("127.0.0.1", 8090).sync();
            // 先连上
            channelFuture.sync();

            OrderOperation orderOperation = new OrderOperation(1001, "tudou");
            channelFuture.channel().writeAndFlush(orderOperation);

            channelFuture.channel().closeFuture().sync();

            System.out.println("Client !");

        } finally {
            group.shutdownGracefully();
        }
    }
}
