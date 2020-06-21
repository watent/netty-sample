package com.sample.order.common.keepalive;

import com.sample.order.common.OperationResult;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * TODO
 *
 * @author JueYi
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class KeepaliveOperationResult extends OperationResult {

    private final long time;
}
