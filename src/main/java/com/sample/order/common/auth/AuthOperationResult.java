package com.sample.order.common.auth;

import com.sample.order.common.OperationResult;
import lombok.Data;

/**
 * TODO
 *
 * @author JueYi
 */
@Data
public class AuthOperationResult extends OperationResult {

    private final boolean passAuth;
}
