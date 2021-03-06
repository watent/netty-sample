package com.sample.order.common.auth;

import com.sample.order.common.Operation;
import lombok.Data;
import lombok.extern.java.Log;

/**
 * 授权
 *
 * @author JueYi
 */
@Data
@Log
public class AuthOperation extends Operation {

    private final String userName;
    private final String password;

    @Override
    public AuthOperationResult execute() {

        if ("admin".equalsIgnoreCase(this.userName)) {
            AuthOperationResult orderResponse = new AuthOperationResult(true);
            return orderResponse;
        }

        return new AuthOperationResult(false);
    }
}
