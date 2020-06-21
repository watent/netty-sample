package com.sample.order.server.codec;

import io.netty.handler.codec.LengthFieldPrepender;

/**
 * OrderFrame Encoder
 * 第二次编码 解决沾包和半包问题
 *
 * @author JueYi
 */
public class OrderFrameEncoder extends LengthFieldPrepender {

    public OrderFrameEncoder() {
        super(2);
    }
}
