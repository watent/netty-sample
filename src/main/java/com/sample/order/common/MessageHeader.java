package com.sample.order.common;

import lombok.Data;

/**
 * 定义消息头
 *
 * @author JueYi
 */

@Data
public class MessageHeader {

    /**
     * 版本号
     */
    private int version = 1;
    /**
     * 操作码
     */
    private int opCode;
    /**
     * 消息ID
     */
    private long streamId;


}
