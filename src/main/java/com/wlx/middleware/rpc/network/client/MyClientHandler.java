package com.wlx.middleware.rpc.network.client;

import com.wlx.middleware.rpc.network.future.SyncWriteMap;
import com.wlx.middleware.rpc.network.future.WriteFuture;
import com.wlx.middleware.rpc.network.msg.Response;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class MyClientHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        Response response = (Response) msg;
        WriteFuture<Response> writeFuture = SyncWriteMap.synKey.get(response.getRequestId());
        writeFuture.setResult(response);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
    }
}
