package com.sample.order.client.codec;

import io.netty.handler.codec.LengthFieldPrepender;

/**
 * OrderFrame Encoder
 *
 * @author JueYi
 */
public class OrderFrameEncoder extends LengthFieldPrepender {

    public OrderFrameEncoder() {
        super(2);
    }
}
