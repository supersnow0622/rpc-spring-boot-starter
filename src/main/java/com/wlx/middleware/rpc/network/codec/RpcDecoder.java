package com.wlx.middleware.rpc.network.codec;

import com.wlx.middleware.rpc.utils.SerializationUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

public class RpcDecoder extends ByteToMessageDecoder {

    private Class<?> genericClazz;

    public RpcDecoder(Class<?> genericClazz) {
        this.genericClazz = genericClazz;
    }

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        if (byteBuf.readableBytes() < 4) {
            return;
        }

        byteBuf.markReaderIndex();
        int size = byteBuf.readInt();
        if (byteBuf.readableBytes() < size) {
            byteBuf.resetReaderIndex();
            return;
        }

        byte[] data = new byte[size];
        byteBuf.readBytes(data);
        list.add(SerializationUtils.deserialize(data, genericClazz));
    }
}
