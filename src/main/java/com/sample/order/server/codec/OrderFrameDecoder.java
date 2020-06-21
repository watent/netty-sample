package com.sample.order.server.codec;

import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

/**
 * OrderFrame Decoder
 * 第一次解码 解决TCP协议中沾包半包问题
 *
 * @author JueYi
 */
public class OrderFrameDecoder extends LengthFieldBasedFrameDecoder {

    public OrderFrameDecoder() {
        super(Integer.MAX_VALUE, 0, 2, 0, 2);
    }
}
