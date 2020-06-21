package com.sample.order.common.keepalive;

import com.sample.order.common.Operation;

/**
 * Keepalive Operation
 *
 * @author JueYi
 */
public class KeepaliveOperation extends Operation {

    private long time;

    public KeepaliveOperation() {
        this.time = System.nanoTime();
    }

    @Override
    public KeepaliveOperationResult execute() {
        KeepaliveOperationResult orderResponse = new KeepaliveOperationResult(time);
        return orderResponse;
    }
}
