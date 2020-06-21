package com.sample.order.common.order;

import com.sample.order.common.OperationResult;
import lombok.Data;

/**
 * Order OperationResult
 *
 * @author JueYi
 */
@Data
public class OrderOperationResult extends OperationResult {

    private final int tableId;

    private final String dish;

    private final boolean complete;
}
