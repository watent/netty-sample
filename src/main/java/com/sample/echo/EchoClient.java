package com.sample.echo;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * EchoClient
 *
 * @author JueYi
 */
@Slf4j
public class EchoClient {

    public static void main(String[] args) {

        EventLoopGroup group = new NioEventLoopGroup();

        try {

            Bootstrap b = new Bootstrap();
            b.group(group)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel sc) throws Exception {
                            ChannelPipeline p = sc.pipeline();
//                            p.addLast(new LoggingHandler(LogLevel.INFO));
//                            p.addLast(new EchoClientHandler());
                        }
                    });

            // Start the client.
            ChannelFuture f = b.connect("127.0.0.1", 8090).sync();

            // Wait until the connection is closed.
//            f.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
            log.error(e.getMessage(), "客户端中断异常");
        } finally {
            // Shut down the event loop to terminate all threads.
            group.shutdownGracefully();
        }

    }

}
