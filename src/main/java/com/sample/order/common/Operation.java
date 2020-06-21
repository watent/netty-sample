package com.sample.order.common;

/**
 * 操作
 *
 * @author JueYi
 */
public abstract class  Operation extends MessageBody {

    public abstract OperationResult execute();
}
