package com.sample.order.server;

import com.sample.order.server.codec.OrderFrameDecoder;
import com.sample.order.server.codec.OrderFrameEncoder;
import com.sample.order.server.codec.OrderProtocolDecoder;
import com.sample.order.server.codec.OrderProtocolEncoder;
import com.sample.order.server.handler.MetricHandler;
import com.sample.order.server.handler.OrderServerProcessHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioChannelOption;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.util.concurrent.DefaultThreadFactory;

import java.util.concurrent.ExecutionException;

/**
 * 下单服务
 * 入站 OrderFrameDecoder -> OrderProtocolDecoder
 * 处理 OrderServerProcessHandler
 * 出站 OrderProtocolEncoder -> OrderFrameEncoder
 *
 * @author JueYi
 */
public class Server {


    public static void main(String[] args) throws InterruptedException, ExecutionException {

        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap.channel(NioServerSocketChannel.class);

        serverBootstrap.handler(new LoggingHandler(LogLevel.INFO));
        NioEventLoopGroup boss = new NioEventLoopGroup(0, new DefaultThreadFactory("boss"));
        NioEventLoopGroup worker = new NioEventLoopGroup(0, new DefaultThreadFactory("worker"));

        MetricHandler metricHandler = new MetricHandler();

        serverBootstrap.childOption(NioChannelOption.TCP_NODELAY, true);
        serverBootstrap.option(NioChannelOption.SO_BACKLOG, 1024);

        try {
            serverBootstrap.group(boss, worker);

            serverBootstrap.childHandler(new ChannelInitializer<SocketChannel>() {

                @Override
                protected void initChannel(SocketChannel ch) throws Exception {

                    ChannelPipeline pipeline = ch.pipeline();

                    pipeline.addLast("frameDecoder", new OrderFrameDecoder());
                    pipeline.addLast(new OrderFrameEncoder());

                    pipeline.addLast(new OrderProtocolEncoder());
                    pipeline.addLast(new OrderProtocolDecoder());

                    pipeline.addLast("metric", metricHandler);

                    pipeline.addLast(new LoggingHandler(LogLevel.INFO));

                    pipeline.addLast(new OrderServerProcessHandler());
                }
            });

            ChannelFuture channelFuture = serverBootstrap.bind(8090).sync();

            channelFuture.channel().closeFuture().get();
//            channelFuture.channel().closeFuture().sync();

            System.out.println("Server start success !");

        } finally {
            boss.shutdownGracefully();
            worker.shutdownGracefully();
        }
    }
}
