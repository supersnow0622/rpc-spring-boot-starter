package com.wlx.middleware.rpc.network.future;

import com.wlx.middleware.rpc.network.msg.Request;
import com.wlx.middleware.rpc.network.msg.Response;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;


import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class SyncWrite {

    public Response writeAndFlush(Channel channel, Request request, long timeout) throws Exception {
        String requestId = UUID.randomUUID().toString();
        request.setRequestId(requestId);

        WriteFuture<Response> writeFuture = new SyncWriteFuture();
        SyncWriteMap.synKey.put(requestId, writeFuture);

        Response response = doWriteAndSync(channel, request, timeout, writeFuture);
        SyncWriteMap.synKey.remove(requestId);

        return response;
    }

    private Response doWriteAndSync(Channel channel, Request request, long timeout,
                                    WriteFuture<Response> writeFuture) throws Exception {
        channel.writeAndFlush(request).addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture channelFuture) throws Exception {
                writeFuture.setWriteSuccess(channelFuture.isSuccess());
                writeFuture.setCause(channelFuture.cause());
                if (!writeFuture.isWriteSuccess()) {
                    SyncWriteMap.synKey.remove(request.getRequestId());
                }
            }
        });

        Response response = writeFuture.get(timeout, TimeUnit.MILLISECONDS);
        if (response == null) {
            if (writeFuture.isTimeout()) {
                throw new TimeoutException();
            } else {
                throw new Exception(writeFuture.cause());
            }
        }
        return response;
    }
}
