package com.wlx.middleware.rpc.network.server;

import com.wlx.middleware.rpc.domain.LocalServerInfo;
import com.wlx.middleware.rpc.network.codec.RpcDecoder;
import com.wlx.middleware.rpc.network.codec.RpcEncoder;
import com.wlx.middleware.rpc.network.msg.Request;
import com.wlx.middleware.rpc.network.msg.Response;
import com.wlx.middleware.rpc.utils.NetUtils;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.springframework.context.ApplicationContext;


public class ServerSocket implements Runnable {

    private transient ApplicationContext applicationContext;

    private ChannelFuture channelFuture;

    public ServerSocket(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    public boolean isActiveSocket() {
        if (channelFuture != null) {
            return channelFuture.channel().isActive();
        }
        return false;
    }


    @Override
    public void run() {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workGroup = new NioEventLoopGroup();
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(bossGroup, workGroup)
                .option(ChannelOption.SO_BACKLOG, 1024)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        socketChannel.pipeline().addLast(new RpcDecoder(Request.class));
                        socketChannel.pipeline().addLast(new RpcEncoder(Response.class));
                        socketChannel.pipeline().addLast(new MyServerHandler(applicationContext));
                    }
                });
        try {
            LocalServerInfo.LOCAL_HOST = NetUtils.getHost();
            int port = 20000;
            while (NetUtils.isPortUsing(port)) {
                port++;
            }
            LocalServerInfo.LOCAL_PORT = port;
            System.out.println("端口号：" + port);
            channelFuture = serverBootstrap.bind(port).sync();
            channelFuture.channel().closeFuture().sync();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            bossGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
        }
    }
}
