package com.sample.order.common;

/**
 * Response Message
 *
 * @author JueYi
 */
@SuppressWarnings("unchecked")
public class ResponseMessage extends Message<OperationResult> {

    @SuppressWarnings(value = {"unchecked", "rawtypes"})
    @Override
    public Class getMessageBodyDecodeClass(int opcode) {
        return OperationType.fromOpCode(opcode).getOperationResultClazz();
    }
}
