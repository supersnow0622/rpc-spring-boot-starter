package com.wlx.middleware.rpc.network.codec;

import com.wlx.middleware.rpc.utils.SerializationUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class RpcEncoder extends MessageToByteEncoder {

    private Class<?> genericClazz;

    public RpcEncoder(Class<?> genericClazz) {
        this.genericClazz = genericClazz;
    }

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Object o, ByteBuf byteBuf) throws Exception {
        if (genericClazz.isInstance(o)) {
            byte[] data = SerializationUtils.serialize(o);
            byteBuf.writeInt(data.length);
            byteBuf.writeBytes(data);
        }
    }
}
