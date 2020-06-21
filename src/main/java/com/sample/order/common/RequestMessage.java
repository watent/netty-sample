package com.sample.order.common;

/**
 * Request Message
 *
 * @author JueYi
 */
@SuppressWarnings("unchecked")
public class RequestMessage extends Message<Operation> {

    public RequestMessage() {
    }

    public RequestMessage(Long streamId, Operation operation) {
        MessageHeader messageHeader = new MessageHeader();
        messageHeader.setStreamId(streamId);
        messageHeader.setOpCode(OperationType.fromOperation(operation).getOpCode());
        this.setMessageHeader(messageHeader);
        this.setMessageBody(operation);
    }

    @SuppressWarnings(value = {"unchecked", "rawtypes"})
    @Override
    public Class getMessageBodyDecodeClass(int opcode) {
        return OperationType.fromOpCode(opcode).getOperationClazz();
    }


}
