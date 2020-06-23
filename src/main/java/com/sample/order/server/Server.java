package com.sample.order.server;

import com.sample.order.server.codec.OrderFrameDecoder;
import com.sample.order.server.codec.OrderFrameEncoder;
import com.sample.order.server.codec.OrderProtocolDecoder;
import com.sample.order.server.codec.OrderProtocolEncoder;
import com.sample.order.server.handler.AuthHandler;
import com.sample.order.server.handler.MetricHandler;
import com.sample.order.server.handler.OrderServerProcessHandler;
import com.sample.order.server.handler.ServerIdleCheckHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioChannelOption;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.flush.FlushConsolidationHandler;
import io.netty.handler.ipfilter.IpFilterRuleType;
import io.netty.handler.ipfilter.IpSubnetFilterRule;
import io.netty.handler.ipfilter.RuleBasedIpFilter;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.SslHandler;
import io.netty.handler.ssl.util.SelfSignedCertificate;
import io.netty.handler.traffic.GlobalTrafficShapingHandler;
import io.netty.util.concurrent.DefaultThreadFactory;
import io.netty.util.concurrent.UnorderedThreadPoolEventExecutor;

import javax.net.ssl.SSLException;
import java.security.cert.CertificateException;
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


    public static void main(String[] args) throws InterruptedException, ExecutionException, CertificateException, SSLException {

        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap.channel(NioServerSocketChannel.class);

        serverBootstrap.handler(new LoggingHandler(LogLevel.INFO));
        NioEventLoopGroup boss = new NioEventLoopGroup(0, new DefaultThreadFactory("boss"));
        NioEventLoopGroup worker = new NioEventLoopGroup(0, new DefaultThreadFactory("worker"));

        NioEventLoopGroup eventLoopGroupForTrafficShaping = new NioEventLoopGroup(0, new DefaultThreadFactory("TS"));

        MetricHandler metricHandler = new MetricHandler();
        UnorderedThreadPoolEventExecutor business = new UnorderedThreadPoolEventExecutor(10, new DefaultThreadFactory("business"));
        NioEventLoopGroup eventExecutors = new NioEventLoopGroup(0, new DefaultThreadFactory("business"));

        serverBootstrap.childOption(NioChannelOption.TCP_NODELAY, true);
        serverBootstrap.option(NioChannelOption.SO_BACKLOG, 1024);

        IpSubnetFilterRule ipSubnetFilterRule = new IpSubnetFilterRule("127.1.0.1", 16, IpFilterRuleType.REJECT);
        RuleBasedIpFilter ruleBasedIpFilter = new RuleBasedIpFilter(ipSubnetFilterRule);

        AuthHandler authHandler = new AuthHandler();

        SelfSignedCertificate selfSignedCertificate = new SelfSignedCertificate();
        System.out.println(selfSignedCertificate.certificate());
        SslContext sslContext = SslContextBuilder.forServer(selfSignedCertificate.certificate(), selfSignedCertificate.privateKey()).build();


        try {
            serverBootstrap.group(boss, worker);

            GlobalTrafficShapingHandler globalTrafficShapingHandler = new GlobalTrafficShapingHandler(eventLoopGroupForTrafficShaping, 10 * 1024 * 1024, 10 * 1024 * 1024);

            serverBootstrap.childHandler(new ChannelInitializer<SocketChannel>() {

                @Override
                protected void initChannel(SocketChannel ch) throws Exception {

                    ChannelPipeline pipeline = ch.pipeline();


                    // ipFilter
                    pipeline.addLast("ipFilter", ruleBasedIpFilter);
                    // trafficShaping
                    pipeline.addLast("tsHandler", globalTrafficShapingHandler);
                    // idle
                    pipeline.addLast("idleHandler", new ServerIdleCheckHandler());

                    SslHandler sslHandler = sslContext.newHandler(ch.alloc());
                    pipeline.addLast("ssl", sslHandler);

                    pipeline.addLast("frameDecoder", new OrderFrameDecoder());
                    pipeline.addLast(new OrderFrameEncoder());
                    pipeline.addLast(new OrderProtocolEncoder());
                    pipeline.addLast(new OrderProtocolDecoder());

                    // 统计
                    pipeline.addLast("metric", metricHandler);
                    // 授权
                    pipeline.addLast("auth", authHandler);

                    pipeline.addLast(new LoggingHandler(LogLevel.INFO));

                    pipeline.addLast("flushEnhance", new FlushConsolidationHandler(5, true));

                    // 从EventLoopGroup中拿出一个绑定 只用其中一个线程 而非线程池
                    // pipeline.addLast( eventExecutors, new OrderServerProcessHandler());
                    pipeline.addLast(business, new OrderServerProcessHandler());


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
