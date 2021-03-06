package com.sample.order.client;

import com.sample.order.client.codec.OrderFrameDecoder;
import com.sample.order.client.codec.OrderFrameEncoder;
import com.sample.order.client.codec.OrderProtocolDecoder;
import com.sample.order.client.codec.OrderProtocolEncoder;
import com.sample.order.client.codec.dispatcher.ClientIdleCheckHandler;
import com.sample.order.client.codec.dispatcher.KeepaliveHandler;
import com.sample.order.common.RequestMessage;
import com.sample.order.common.auth.AuthOperation;
import com.sample.order.common.order.OrderOperation;
import com.sample.order.util.IdUtil;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioChannelOption;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;

import javax.net.ssl.SSLException;

/**
 * 下单服务
 *
 * @author JueYi
 */
public class Client {


    public static void main(String[] args) throws InterruptedException, SSLException {

        Bootstrap bootstrap = new Bootstrap();
        bootstrap.channel(NioSocketChannel.class);
        bootstrap.option(NioChannelOption.CONNECT_TIMEOUT_MILLIS, 10 * 1000);

        NioEventLoopGroup group = new NioEventLoopGroup();

        KeepaliveHandler keepaliveHandler = new KeepaliveHandler();

        SslContext sslContext = SslContextBuilder.forClient().build();

        try {
            bootstrap.group(group);

            bootstrap.handler(new ChannelInitializer<SocketChannel>() {

                @Override
                protected void initChannel(SocketChannel ch) throws Exception {

                    ChannelPipeline pipeline = ch.pipeline();

                    pipeline.addLast(new ClientIdleCheckHandler());

                    pipeline.addLast(sslContext.newHandler(ch.alloc()));

                    pipeline.addLast(new OrderFrameDecoder());
                    pipeline.addLast(new OrderFrameEncoder());

                    pipeline.addLast(new OrderProtocolEncoder());
                    pipeline.addLast(new OrderProtocolDecoder());

                    pipeline.addLast(new LoggingHandler(LogLevel.INFO));

                    pipeline.addLast(keepaliveHandler);

                }
            });

            ChannelFuture channelFuture = bootstrap.connect("127.0.0.1", 8090).sync();
            // 先连上
            channelFuture.sync();


            RequestMessage firstAuthMessage = new RequestMessage(IdUtil.nextId(), new AuthOperation("admin", "123456"));
            channelFuture.channel().writeAndFlush(firstAuthMessage);

            RequestMessage requestMessage = new RequestMessage(IdUtil.nextId(), new OrderOperation(1001, "tudou"));

            // 发送一万次 演示内存泄漏
//            for (int i = 0; i < 20; i++) {
//                channelFuture.channel().writeAndFlush(requestMessage);
//            }

            channelFuture.channel().writeAndFlush(requestMessage);

            channelFuture.channel().closeFuture().sync();

            System.out.println("Client !");

        } finally {
            group.shutdownGracefully();
        }
    }
}
