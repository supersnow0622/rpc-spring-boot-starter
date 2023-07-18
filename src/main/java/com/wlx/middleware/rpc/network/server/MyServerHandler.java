package com.wlx.middleware.rpc.network.server;

import com.wlx.middleware.rpc.network.msg.Request;
import com.wlx.middleware.rpc.network.msg.Response;
import com.wlx.middleware.rpc.utils.ClassLoaderUtils;
import com.wlx.middleware.rpc.utils.SerializationUtils;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.springframework.context.ApplicationContext;

import java.lang.reflect.Method;

public class MyServerHandler extends ChannelInboundHandlerAdapter {

    private ApplicationContext applicationContext;

    public MyServerHandler(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        Request request = (Request) msg;
        // 通过反射调用接口实现
        Object bean = applicationContext.getBean(request.getRef());
        Class clazz = ClassLoaderUtils.forName(((Request) msg).getInterfaces());
        Method method = clazz.getMethod(request.getMethodName(), request.getParamTypes());
        Object result = method.invoke(bean, request.getArgs());

        // 封装结果信息
        Response response = new Response();
        response.setRequestId(request.getRequestId());
        response.setResult(result);
        ctx.writeAndFlush(response);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }
}
