package com.sample.order.util;

import java.util.concurrent.atomic.AtomicLong;

/**
 * ID生成器
 *
 * @author JueYi
 */
public class IdUtil {

    private static final AtomicLong IDX = new AtomicLong();

    private IdUtil() {
    }

    public static long nextId() {
        return IDX.incrementAndGet();
    }
}
