package com.wlx.middleware.rpc.network.client;

import com.wlx.middleware.rpc.network.codec.RpcDecoder;
import com.wlx.middleware.rpc.network.codec.RpcEncoder;
import com.wlx.middleware.rpc.network.msg.Request;
import com.wlx.middleware.rpc.network.msg.Response;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

public class ClientSocket implements Runnable {

    private ChannelFuture channelFuture;

    private String host;

    private int port;

    public ClientSocket(String host, int port) {
        this.host = host;
        this.port = port;
    }

    @Override
    public void run() {
        EventLoopGroup eventLoopGroup = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap()
                .group(eventLoopGroup).option(ChannelOption.AUTO_READ, true)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        socketChannel.pipeline().addLast(new RpcDecoder(Response.class));
                        socketChannel.pipeline().addLast(new RpcEncoder(Request.class));
                        socketChannel.pipeline().addLast(new MyClientHandler());
                    }
                });
        try {
            channelFuture = bootstrap.connect(host, port).sync();
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            eventLoopGroup.shutdownGracefully();
        }
    }

    public ChannelFuture getChannelFuture() {
        return channelFuture;
    }
}
