package com.sample.order.client.codec.dispatcher;

import io.netty.handler.timeout.IdleStateHandler;

import java.util.concurrent.TimeUnit;

/**
 * 客户端Idle检测
 *
 * @author JueYi
 */
public class ClientIdleCheckHandler extends IdleStateHandler {

    public ClientIdleCheckHandler() {
        super(0, 5, 0, TimeUnit.SECONDS);
    }
}
