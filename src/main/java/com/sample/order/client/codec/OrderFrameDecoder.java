package com.sample.order.client.codec;

import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

/**
 * OrderFrame Decoder
 *
 * @author JueYi
 */
public class OrderFrameDecoder extends LengthFieldBasedFrameDecoder {

    public OrderFrameDecoder() {
        super(Integer.MAX_VALUE, 0, 2, 0, 2);
    }
}
