package com.sample.order.common;

import com.sample.order.util.JsonUtil;
import io.netty.buffer.ByteBuf;
import lombok.Data;

import java.nio.charset.StandardCharsets;

import static com.sample.order.util.JsonUtil.fromJson;

/**
 * 定义消息
 *
 * @author JueYi
 */
@Data
public abstract class Message<T extends MessageBody> {

    private MessageHeader messageHeader;

    private T messageBody;

    public T getMessageBody() {
        return messageBody;
    }

    /**
     * 编码
     */
    public void encode(ByteBuf byteBuf) {

        byteBuf.writeInt(messageHeader.getVersion());
        byteBuf.writeLong(messageHeader.getStreamId());
        byteBuf.writeInt(messageHeader.getOpCode());
        byteBuf.writeBytes(JsonUtil.toJson(messageBody).getBytes());
    }

    public abstract Class<T> getMessageBodyDecodeClass(int opCode);

    /**
     * 解码
     */
    public void decode(ByteBuf msg) {

        int version = msg.readInt();
        long streamId = msg.readLong();
        int opCode = msg.readInt();

        MessageHeader messageHeader = new MessageHeader();
        messageHeader.setVersion(version);
        messageHeader.setOpCode(opCode);
        messageHeader.setStreamId(streamId);
        this.messageHeader = messageHeader;

        Class<T> bodyClazz = getMessageBodyDecodeClass(opCode);
        this.messageBody = fromJson(msg.toString(StandardCharsets.UTF_8), bodyClazz);
    }


}
